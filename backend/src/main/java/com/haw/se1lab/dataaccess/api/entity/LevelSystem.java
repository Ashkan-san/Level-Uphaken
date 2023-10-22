package com.haw.se1lab.dataaccess.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.haw.se1lab.common.api.exception.LevelSystemAlreadyInQuestException;
import com.haw.se1lab.common.api.exception.QuestAlreadyInLevelSystemException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a level system for personal overview. Students can see their level and xp.
 *
 * @author Kathleen Neitzel
 * @author Philip Gisella
 */

@Entity
public class LevelSystem {

    public static final List<Level> defaultLevels = Arrays.asList(
            new Level(1, "Newbie", 150),
            new Level(2, "Apprentice", 250),
            new Level(3, "Scholar", 500),
            new Level(4, "Legend", 1000)
            );

    /* ---- Member Fields ---- */

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    int xp = 0;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JoinTable(name = "level_system_finished_quests", joinColumns = @JoinColumn(name = "level_system_id"), inverseJoinColumns = @JoinColumn(name = "quest_id"))
    @JsonIgnoreProperties({ "finishedLevelSystems" })
    private final List<Quest> finishedQuests = new ArrayList<>();

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "levelsystem", "subjects" })
    private User user;

    /* ---- Constructors ---- */

    public LevelSystem() {
    }

    /* ---- Getters/Setters ---- */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public Level getLevel() {
        // Inefficient
        return defaultLevels.stream().filter(elm -> this.xp < elm.xpThreshold).reduce((first, second) -> first).orElseThrow(() -> new IllegalStateException("Unknown Level with XP: " + this.xp));
    }

    @JsonProperty("level")
    public int getLevelNumber() {
        return getLevel().levelNumber;
    }

    public String getLevelName() {
        return getLevel().name;
    }

    public int getMissingXP() {
        return getLevel().getMissingXp(this.xp);
    }

    public int getXp() {
        return xp;
    }

    public List<Quest> getFinishedQuests() {
        return finishedQuests;
    }

    /* ---- Custom Methods ---- */

    public void addQuest(Quest quest) throws QuestAlreadyInLevelSystemException, LevelSystemAlreadyInQuestException {
        if (finishedQuests.contains(quest)) throw new QuestAlreadyInLevelSystemException(quest.getTitle());

        finishedQuests.add(quest);
        increaseXP(quest.rewardXP);
        if (!quest.getFinishedLevelSystems().contains(this)) {
            quest.addLevelSystem(this);
        }
    }

    public void removeQuest(Quest quest) {
        if (!finishedQuests.contains(quest)) return;

        finishedQuests.remove(quest);
        decreaseXP(quest.rewardXP);
        if (quest.getFinishedLevelSystems().contains(this)) {
            quest.removeLevelSystem(this);
        }
    }

    public void increaseXP(int xp) {
        this.xp += xp;
    }

    public void decreaseXP(int xp) {
        this.xp -= xp;
        if (this.xp < 0) {
            this.xp = 0;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user != null && getUser() == null) {
            this.user = user;
            user.setLevelsystem(this);
        }
    }
    /* ---- Overridden Methods ---- */

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelSystem that = (LevelSystem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    static class Level {

        private final int levelNumber;
        private final String name;
        private final int xpThreshold;

        public Level(int levelNumber, String name, int xpThreshold) {
            this.levelNumber = levelNumber;
            this.name = name;
            this.xpThreshold = xpThreshold;
        }

        public int getLevelNumber() {
            return levelNumber;
        }

        public String getName() {
            return name;
        }

        public int getXpThreshold() {
            return xpThreshold;
        }

        public int getMissingXp(int xp) {
            return xpThreshold - xp;
        }

        @Override
        public String toString() {
            return name + " (Level " + levelNumber + ")";
        }
    }
}
