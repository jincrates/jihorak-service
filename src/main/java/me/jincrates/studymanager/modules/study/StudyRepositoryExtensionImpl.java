package me.jincrates.studymanager.modules.study;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import me.jincrates.studymanager.modules.tag.QTag;
import me.jincrates.studymanager.modules.tag.Tag;
import me.jincrates.studymanager.modules.zone.QZone;
import me.jincrates.studymanager.modules.zone.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension{

    //상위 클래스에 default 생성자가 없기 때문에 아래와 같은 생성자를 만들어줘야 컴파일 에러가 사라진다.
    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public Page<Study> findByKeyword(String keyword, Pageable pageable) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study).where(study.published.isTrue()
                .and(study.title.containsIgnoreCase(keyword))
                .or(study.tags.any().title.containsIgnoreCase(keyword))
                .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
                .leftJoin(study.tags, QTag.tag).fetchJoin()
                .leftJoin(study.zones, QZone.zone).fetchJoin()
                //.leftJoin(study.members, QAccount.account).fetchJoin()  // memberCount라는 필드를 추가하였기에 조인을 하지 않는다.
                .distinct(); //TODO: 쿼리 자체에서 distinct말고 result transformer로 튜닝
        JPQLQuery<Study> pageableQuery = Objects.requireNonNull(getQuerydsl())
                .applyPagination(pageable, query);
        QueryResults<Study> fetchResults = pageableQuery.fetchResults();//fetchResults를 써야 페이징 관련 정보도 가져온다.
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public List<Study> findByAccountWithTagAndZone(Set<Tag> tags, Set<Zone> zones) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study).where(study.published.isTrue()
                .and(study.closed.isFalse())
                .and(study.tags.any().in(tags))
                .and(study.zones.any().in(zones)))
                .leftJoin(study.tags, QTag.tag).fetchJoin()
                .leftJoin(study.zones, QZone.zone).fetchJoin()
                .orderBy(study.publishedAt.desc())
                .distinct()
                .limit(6);
        return query.fetch();
    }


}
