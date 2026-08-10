CREATE DOMAIN non_empty_varchar_255 VARCHAR(255)
    CHECK (btrim(VALUE) <> '');

CREATE TABLE users(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username non_empty_varchar_255 NOT NULL,
    password_hash non_empty_varchar_255 NOT NULL,
    email non_empty_varchar_255 NOT NULL,
    avatar_url non_empty_varchar_255 NOT NULL
);

CREATE TABLE roles(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name non_empty_varchar_255 NOT NULL UNIQUE
);

CREATE TABLE user_roles(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT pk_user_roles
        PRIMARY KEY(user_id, role_id),

    CONSTRAINT fk_user_roles_user_id
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role_id
        FOREIGN KEY(role_id)
        REFERENCES roles(id)
        ON DELETE CASCADE
);

CREATE TABLE permissions(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code non_empty_varchar_255 NOT NULL UNIQUE,
    description non_empty_varchar_255 NOT NULL
);

CREATE TABLE role_permissions(
    permission_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT pk_role_permissions
        PRIMARY KEY(role_id, permission_id),

    CONSTRAINT fk_role_permissions_permission_id
        FOREIGN KEY(permission_id)
        REFERENCES permissions(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_role_permissions_role_id
        FOREIGN KEY(role_id)
        REFERENCES roles(id)
        ON DELETE CASCADE
);

CREATE TABLE friendships(
    sender_id BIGINT NOT NULL,
    acceptor_id BIGINT NOT NULL,
    status non_empty_varchar_255 NOT NULL,

    CONSTRAINT pk_friendships
        PRIMARY KEY(sender_id, acceptor_id),

    CONSTRAINT fk_friendships_sender_id
        FOREIGN KEY(sender_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_friendships_acceptor_id
        FOREIGN KEY(acceptor_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_friendships_status
        CHECK (status IN ('pending', 'cancelled', 'accepted')),

    CONSTRAINT chk_friendships_users
        CHECK (acceptor_id <> sender_id)
);

CREATE TABLE games(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name non_empty_varchar_255 NOT NULL,
    best_count_players JSONB NOT NULL,
    image_url non_empty_varchar_255 NOT NULL
);

CREATE TABLE user_games(
    user_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,

    CONSTRAINT pk_user_games
        PRIMARY KEY(user_id, game_id),

    CONSTRAINT fk_user_games_user_id
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_games_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE RESTRICT
);

CREATE TABLE sessions(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    game_id BIGINT NOT NULL,
    time TIMESTAMPTZ NOT NULL,
    place non_empty_varchar_255,
    status non_empty_varchar_255 NOT NULL,

    CONSTRAINT fk_sessions_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_sessions_status
        CHECK (status IN ('scheduled', 'in progress', 'finished', 'cancelled'))
);

CREATE TABLE session_participants(
    user_id BIGINT,
    session_id BIGINT NOT NULL,
    score BIGINT NOT NULL DEFAULT 0,
    is_winner BOOLEAN NOT NULL DEFAULT FALSE,
    guest_name non_empty_varchar_255, /* used when user don't have polka account */

    CONSTRAINT pk_session_participants
        PRIMARY KEY(user_id, session_id),

    CONSTRAINT fk_session_participants_user_id
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_session_participants_session_id
        FOREIGN KEY(session_id)
        REFERENCES sessions(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_session_participants_user
        CHECK ((user_id IS NULL AND guest_name IS NOT NULL) OR (user_ID IS NOT NULL AND guest_name IS NULL))
);