package me.jincrates.studymanager.modules;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import me.jincrates.studymanager.App;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = App.class)
public class PackageDependencyTests {

    private static final String ACCOUNT = "..modules.account..";
    private static final String EVENT = "..modules.event..";
    private static final String STUDY = "..modules.study..";
    private static final String TAG = "..modules.tag..";
    private static final String ZONE = "..modules.zone..";
    private static final String MAIN = "..modules.main..";

    @ArchTest
    ArchRule modulesPackageRule = classes().that().resideInAPackage("me.jincrates.studymanager.modules..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage("me.jincrates.studymanager.modules..");

    //@ArchTest  //쿼리 dsl을 반영하면서 의존성이 생긴 것으로 파악. 추후 수정 예정
    ArchRule studyPackageRule = classes().that().resideInAPackage(STUDY)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(STUDY, EVENT, MAIN);

    @ArchTest
    ArchRule eventPackageRule = classes().that().resideInAPackage(EVENT)
            .should().accessClassesThat().resideInAnyPackage(STUDY, ACCOUNT, EVENT);

    @ArchTest
    ArchRule accountPackageRule = classes().that().resideInAPackage(ACCOUNT)
            .should().accessClassesThat().resideInAnyPackage(TAG, ZONE, ACCOUNT);


    //순환참조 체크, 순환참조를 하고 있으면 모듈화하기 어렵다.
    @ArchTest
    ArchRule cycleCheck = slices().matching("me.jincrates.studymanager.modules.(*)..")
            .should().beFreeOfCycles();
}
