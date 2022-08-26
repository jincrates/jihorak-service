package me.jincrates.studymanager.modules.study;

import me.jincrates.studymanager.modules.tag.Tag;
import me.jincrates.studymanager.modules.zone.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    Page<Study> findByKeyword(String keyword, Pageable pageable);

    List<Study> findByAccountWithTagAndZone(Set<Tag> tags, Set<Zone> zones);
}
