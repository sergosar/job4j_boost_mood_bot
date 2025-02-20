CREATE TABLE mb_award
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    days        INT          NOT NULL
);
CREATE TABLE mb_mood
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(255),
    good BOOLEAN NOT NULL
);
CREATE TABLE mb_mood_content
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    mood_id BIGINT,
    text    VARCHAR(255),
    CONSTRAINT fk_mb_mood_content_mood FOREIGN KEY (mood_id) REFERENCES mb_mood (id) ON DELETE CASCADE
);
CREATE TABLE mb_user
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT UNIQUE NOT NULL,
    chat_id   BIGINT        NOT NULL
);

CREATE TABLE mb_mood_log
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT,
    mood_id    BIGINT,
    created_at BIGINT NOT NULL,
    CONSTRAINT fk_mb_mood_log_user FOREIGN KEY (user_id) REFERENCES mb_user (id) ON DELETE SET NULL,
    CONSTRAINT fk_mb_mood_log_mood FOREIGN KEY (mood_id) REFERENCES mb_mood (id) ON DELETE SET NULL
);



CREATE TABLE mb_achievement
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    create_at BIGINT NOT NULL,
    user_id   BIGINT,
    award_id  BIGINT,
    CONSTRAINT fk_mb_achievement_user FOREIGN KEY (user_id) REFERENCES mb_user (id) ON DELETE SET NULL,
    CONSTRAINT fk_mb_achievement_award FOREIGN KEY (award_id) REFERENCES mb_award (id) ON DELETE SET NULL
);