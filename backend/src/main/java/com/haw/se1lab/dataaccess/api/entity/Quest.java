package com.haw.se1lab.dataaccess.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.haw.se1lab.common.api.datatype.QuestType;
import com.haw.se1lab.common.api.exception.LevelSystemAlreadyInQuestException;
import com.haw.se1lab.common.api.exception.QuestAlreadyInLevelSystemException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Quest {

    /* ---- Member Fields ---- */

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    String title;

    @NotNull
    QuestType content;

    @NotNull
    int rewardXP;

    @ManyToMany(mappedBy = "finishedQuests", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "finishedLevelSystems" })
    private final List<LevelSystem> finishedLevelSystems = new ArrayList<>();

    /* ---- Constructors ---- */

    public Quest(){}

    public Quest(String title, QuestType content, int rewardXP) {
        this.title = title;
        this.content = content;
        this.rewardXP = rewardXP;
    }

    /* ---- Getters/Setters ---- */

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuestType getContent() {
        return content;
    }

    public void setContent(QuestType content) {
        this.content = content;
    }

    public int getRewardXP(){
        return rewardXP;
    }

    public void setRewardXP(int rewardXP){
        this.rewardXP = rewardXP;
    }

    public List<LevelSystem> getFinishedLevelSystems() {
        return finishedLevelSystems;
    }

    /* ---- Custom Methods ---- */

    public void addLevelSystem(LevelSystem levelSystem) throws QuestAlreadyInLevelSystemException, LevelSystemAlreadyInQuestException {
        if (finishedLevelSystems.contains(levelSystem)) throw new LevelSystemAlreadyInQuestException(levelSystem.getId());

        finishedLevelSystems.add(levelSystem);
        if (!levelSystem.getFinishedQuests().contains(this)) {
            levelSystem.addQuest(this);
        }
    }

    public void removeLevelSystem(LevelSystem levelSystem){
        if (!finishedLevelSystems.contains(levelSystem)) return;

        finishedLevelSystems.remove(levelSystem);
        if (levelSystem.getFinishedQuests().contains(this)) {
            levelSystem.removeQuest(this);
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
        Quest quest = (Quest) o;
        return id == quest.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
