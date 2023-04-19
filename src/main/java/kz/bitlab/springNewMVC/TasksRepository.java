package kz.bitlab.springNewMVC;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface TasksRepository extends JpaRepository<Tasks, Long> {
    Tasks findAllById(Long id);
}
