package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.common.api.datatype.TaskId;
import com.haw.se1lab.dataaccess.api.entity.Memo;
import com.haw.se1lab.dataaccess.api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Represents a repository for the management of {@link Task} entities in a
 * database.
 *
 * @author Isabell Schettler
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Returns the {@link Memo} entity with the given id.
     *
     * @param id is the id of the memo
     * @return an {@link Optional} containing the found memo
     */
    @Query("select t from Task t where t.id = :id")
    Optional<Task> findById(@Param("id") long id);

    /**
     * Returns the {@link Memo} entity with the given id.
     *
     * @param taskId is the id of the memo
     * @return an {@link Optional} containing the found memo
     */
    @Query("select t from Task t where t.taskId = :taskId")
    Optional<Task> findByTaskId(@Param("taskId") TaskId taskId);


    /**
     * Returns the {@link Task} entity with the given title.
     *
     * @param title is the title of the task
     * @return an {@link Optional} containing the found Task
     */
    @Query("select t from Task t where t.title = :title")
    Optional<Task> findByTitle(@Param("title") String title);

    /**
     * Returns the {@link Task} entities with the given memo.
     *
     * @param memo the memo to search for
     * @return the found tasks
     */
    @Query("select t from Task t where t.memo = :memo")
    List<Task> findByMemo(@Param("memo") Memo memo);

    /**
     * Löscht die {@link Task} Entität mit der gegebenen Id
     *
     * @param id die Id der Task
     */
    @Transactional
    void deleteById(long id);

    /**
     * Löscht die {@link Task} Entität mit der gegebenen Id
     *
     * @param taskId die Id der Task
     */
    @Transactional
    void deleteByTaskId(TaskId taskId);
}
