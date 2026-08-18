CREATE DOMAIN non_empty_varchar_255 VARCHAR(255)
    CHECK (btrim(VALUE) <> '');

CREATE TABLE users(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bgg_id BIGINT UNIQUE,
    login non_empty_varchar_255 NOT NULL,
    username non_empty_varchar_255 NOT NULL,
    password_hash non_empty_varchar_255 NOT NULL,
    email non_empty_varchar_255 NOT NULL,

    CONSTRAINT uq_users_login UNIQUE(login),

    CONSTRAINT uq_users_email UNIQUE(email)
);

CREATE TABLE auth_sessions(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    refresh_token_hash non_empty_varchar_255 NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ
);

CREATE TABLE images(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bucket non_empty_varchar_255 NOT NULL,
    object_key VARCHAR(512) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_images_bucket_object_key
        UNIQUE(bucket, object_key)
);

CREATE TABLE user_images(
    user_id  BIGINT NOT NULL,
    image_id BIGINT NOT NULL,

    CONSTRAINT pk_user_images
        PRIMARY KEY (user_id, image_id),

    CONSTRAINT fk_user_images_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_images_image_id
        FOREIGN KEY (image_id)
            REFERENCES images (id)
            ON DELETE CASCADE
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
        CHECK (status IN ('friend', 'request', 'rejected')),

    CONSTRAINT chk_friendships_users
        CHECK (acceptor_id <> sender_id)
);

CREATE TABLE games(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bgg_id BIGINT UNIQUE,

    name non_empty_varchar_255 NOT NULL,
    description VARCHAR(1024),

    bgg_rating FLOAT,
    polka_rating FLOAT,

    best_count_players INTEGER[],
    available_count_players INTEGER[],

    min_play_time_minutes INTEGER,
    max_play_time_minutes INTEGER,

    min_age INTEGER,

    weight FLOAT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE game_images(
    game_id BIGINT NOT NULL,
    image_id BIGINT NOT NULL,

    CONSTRAINT pk_game_images
        PRIMARY KEY(game_id, image_id),

    CONSTRAINT fk_game_images_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_game_images_image_id
        FOREIGN KEY(image_id)
        REFERENCES images(id)
        ON DELETE CASCADE
);

CREATE TABLE designers(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bgg_id BIGINT UNIQUE,
    name non_empty_varchar_255 NOT NULL
);

CREATE TABLE game_designers(
    game_id BIGINT NOT NULL,
    designer_id BIGINT NOT NULL,

    CONSTRAINT pk_game_designers
        PRIMARY KEY(game_id, designer_id),

    CONSTRAINT fk_game_designers_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_game_designers_designer_id
        FOREIGN KEY(designer_id)
        REFERENCES designers(id)
        ON DELETE CASCADE
);

CREATE TABLE artists(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bgg_id BIGINT UNIQUE,
    name non_empty_varchar_255 NOT NULL
);

CREATE TABLE game_artists(
    game_id BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,

    CONSTRAINT pk_game_artists
        PRIMARY KEY(game_id, artist_id),

    CONSTRAINT fk_game_artists_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_game_artists_artist_id
        FOREIGN KEY(artist_id)
        REFERENCES artists(id)
        ON DELETE CASCADE
);

CREATE TABLE publishers(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bgg_id BIGINT UNIQUE,
    name non_empty_varchar_255 NOT NULL
);

CREATE TABLE game_publishers(
    game_id BIGINT NOT NULL,
    publisher_id BIGINT NOT NULL,

    CONSTRAINT pk_game_publishers
        PRIMARY KEY(game_id, publisher_id),

    CONSTRAINT fk_game_publishers_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_game_publishers_publisher_id
        FOREIGN KEY(publisher_id)
        REFERENCES publishers(id)
        ON DELETE CASCADE
);

CREATE TABLE categories(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bgg_id BIGINT UNIQUE,
    name non_empty_varchar_255 NOT NULL
);

CREATE TABLE game_categories(
    game_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    CONSTRAINT pk_game_categories
        PRIMARY KEY(game_id, category_id),

    CONSTRAINT fk_game_categories_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_game_categories_category_id
        FOREIGN KEY(category_id)
        REFERENCES categories(id)
        ON DELETE CASCADE
);

CREATE TABLE mechanics(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    bgg_id BIGINT UNIQUE,
    name non_empty_varchar_255 NOT NULL
);

CREATE TABLE game_mechanics(
    game_id BIGINT NOT NULL,
    mechanic_id BIGINT NOT NULL,

    CONSTRAINT pk_game_mechanics
        PRIMARY KEY(game_id, mechanic_id),

    CONSTRAINT fk_game_mechanics_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_game_mechanics_mechanics_id
        FOREIGN KEY(mechanic_id)
        REFERENCES mechanics(id)
        ON DELETE CASCADE
);

CREATE TABLE sessions(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    game_id BIGINT NOT NULL,

    creator_id BIGINT NOT NULL,

    note VARCHAR(1024),

    place non_empty_varchar_255,
    status non_empty_varchar_255 NOT NULL,

    started_at TIMESTAMPTZ,
    finished_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_sessions_creator_id
        FOREIGN KEY(creator_id)
        REFERENCES users(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_sessions_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_sessions_status
        CHECK (status IN ('scheduled', 'started', 'finished', 'cancelled')),

    CONSTRAINT chk_sessions_started_earlier_finished
        CHECK (finished_at IS NULL OR started_at < finished_at OR (finished_at IS NOT NULL AND started_at IS NULL))
);

CREATE TABLE session_images(
    session_id BIGINT NOT NULL,
    image_id BIGINT NOT NULL,

    CONSTRAINT pk_session_images
        PRIMARY KEY(session_id, image_id),

    CONSTRAINT fk_session_images_session_id
        FOREIGN KEY(session_id)
        REFERENCES sessions(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_session_images_image_id
        FOREIGN KEY(image_id)
        REFERENCES images(id)
        ON DELETE CASCADE
);

CREATE TABLE session_participants(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT,
    session_id BIGINT NOT NULL,
    score INTEGER NOT NULL DEFAULT 0,
    is_winner BOOLEAN NOT NULL DEFAULT FALSE,
    guest_name non_empty_varchar_255, /* used when user don't have polka account */

    CONSTRAINT uq_session_participants_users
        UNIQUE(user_id, session_id),

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

CREATE TABLE collection_items(
    owner_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,

    display_order FLOAT NOT NULL DEFAULT 0,
    note VARCHAR(1024),
    rating INTEGER,
    status TEXT[] NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_collection_items
        PRIMARY KEY(owner_id, game_id),

    CONSTRAINT fk_collection_items_owner_id
        FOREIGN KEY(owner_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_collection_items_game_id
        FOREIGN KEY(game_id)
        REFERENCES games(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_collection_items_status
        CHECK (
            status <@ ARRAY[
                'OWN',
                'PREVIOUSLY_OWNED',
                'FOR_TRADE',
                'WANT_IN_TRADE',
                'WANT_TO_PLAY',
                'WANT_TO_BUY',
                'PREORDERED',
                'WISHLIST_MUST_HAVE',
                'WISHLIST_LOVE_TO_HAVE',
                'WISHLIST_LIKE_TO_HAVE',
                'WISHLIST_THINKING',
                'WISHLIST_DO_NOT_BUY'
                ]::TEXT[]
            ),

    CONSTRAINT chk_collection_items_rating
        CHECK (rating IS NULL OR rating BETWEEN 1 AND 10)
);

CREATE TABLE collection_item_images(
    owner_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    image_id BIGINT NOT NULL,

    CONSTRAINT pk_collection_items_images
        PRIMARY KEY(owner_id, game_id, image_id),

    CONSTRAINT fk_collection_items_images_owner_id
        FOREIGN KEY(owner_id, game_id)
        REFERENCES collection_items(owner_id, game_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_collection_items_images_image_id
        FOREIGN KEY(image_id)
        REFERENCES images(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_game_designers_designer_id
    ON game_designers(designer_id);

CREATE INDEX idx_game_artists_artist_id
    ON game_artists(artist_id);

CREATE INDEX idx_game_publishers_publisher_id
    ON game_publishers(publisher_id);

CREATE INDEX idx_game_categories_category_id
    ON game_categories(category_id);

CREATE INDEX idx_game_mechanics_mechanic_id
    ON game_mechanics(mechanic_id);

