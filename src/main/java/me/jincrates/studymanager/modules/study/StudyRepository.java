package me.jincrates.studymanager.modules.study;

import me.jincrates.studymanager.modules.account.Account;
import me.jincrates.studymanager.modules.tag.Tag;
import me.jincrates.studymanager.modules.zone.Zone;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryExtension {

    boolean existsByPath(String path);

    @EntityGraph(attributePaths = {"tags", "zones", "managers", "members"}, type = EntityGraph.EntityGraphType.LOAD)
    Study findByPath(String path);

    //With@@는 JPA가 읽지 않는다. 의미상 구분을 짓기위해 작성했으며, findStudyByPath를 찾는 함수가 동작한다.
    @EntityGraph(attributePaths = {"tags", "managers"})
    Study findStudyWithTagsByPath(String path);

    @EntityGraph(attributePaths = {"zones", "managers"})
    Study findStudyWithZonesByPath(String path);

    @EntityGraph(attributePaths = "managers")
    Study findStudyWithManagersByPath(String path);

    @EntityGraph(attributePaths = "members")
    Study findStudyWithMembersByPath(String path);

    Study findStudyOnlyByPath(String path);

    @EntityGraph(attributePaths = {"zones", "tags"})
    Study findStudyWithTagsAndZonesById(Long id);

    @EntityGraph(attributePaths = {"members", "managers"})
    Study findStudyWithManagersAndMemberById(Long id);

    @EntityGraph(attributePaths = {"zones", "tags"})
    List<Study> findFirst9ByPublishedAndClosedOrderByPublishedAtDesc(boolean published, boolean closed);

    @EntityGraph(attributePaths = {"zones", "tags"})
    List<Study> findFirst6ByPublishedAndClosedOrderByPublishedAtDesc(boolean published, boolean closed);

    List<Study> findFirst5ByManagersContainingAndClosedOrderByPublishedAtDesc(Account accountLoaded, boolean closed);

    List<Study> findFirst5ByMembersContainingAndClosedOrderByPublishedAtDesc(Account accountLoaded, boolean closed);
}
