//package io.choerodon.wiki.api.controller
//
//import io.choerodon.core.domain.Page
//import io.choerodon.wiki.IntegrationTestConfiguration
//import io.choerodon.wiki.api.eventhandler.WikiEventHandler
//import io.choerodon.wiki.app.service.WikiSpaceService
//import io.choerodon.wiki.domain.application.entity.ProjectE
//import io.choerodon.wiki.domain.application.entity.WikiSpaceE
//import io.choerodon.wiki.domain.application.entity.iam.OrganizationE
//import io.choerodon.wiki.domain.application.entity.iam.UserE
//import io.choerodon.wiki.domain.application.repository.IamRepository
//import io.choerodon.wiki.domain.application.repository.WikiSpaceRepository
//import io.choerodon.wiki.domain.service.IWikiGroupService
//import io.choerodon.wiki.domain.service.IWikiUserService
//import io.choerodon.wiki.infra.dataobject.iam.OrganizationDO
//import io.choerodon.wiki.infra.dataobject.iam.UserDO
//import io.choerodon.wiki.infra.feign.IamServiceClient
//import io.choerodon.wiki.infra.mapper.WikiSpaceMapper
//import io.choerodon.wiki.infra.persistence.impl.IamRepositoryImpl
//import io.choerodon.wiki.infra.persistence.impl.WikiSpaceRepositoryImpl
//import org.junit.Assert
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.web.client.TestRestTemplate
//import org.springframework.context.annotation.Import
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import spock.lang.Shared
//import spock.lang.Specification
//
//import java.lang.reflect.Field
//
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
//
///**
// * Created by Zenger on 2018/7/25.
// */
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//@Import(IntegrationTestConfiguration)
//class WikiScanningControllerSpec extends Specification {
//
//    @Autowired
//    private TestRestTemplate restTemplate
//
//    @Autowired
//    private WikiEventHandler wikiEventHandler
//
//    @Autowired
//    private IWikiUserService iWikiUserService
//
//    @Autowired
//    private IWikiGroupService iWikiGroupService
//
//    @Autowired
//    private IamRepository iamRepository
//
//    @Autowired
//    private WikiSpaceRepositoryImpl wikiSpaceRepositoryImpl
//
//    @Autowired
//    private WikiSpaceService wikiSpaceService
//
//    @Shared
//    def OrganizationE organizationE
//
//    @Shared
//    def OrganizationDO organizationDO
//
//    @Shared
//    def ProjectE projectE
//
//    @Shared
//    IamServiceClient iamServiceClient
//
//    @Shared
//    WikiSpaceMapper wikiSpaceMapper
//
//    void setup() {
//        UserDO userDO = new UserDO()
//        userDO.setId(1L)
//        userDO.setEmail("test@org.com")
//        userDO.setLoginName("test")
//
//        organizationE = new OrganizationE()
//        organizationE.setId(1)
//        organizationE.setCode("org")
//        organizationE.setName("测试组织")
//        organizationE.setEnabled(true)
//        organizationE.setProjectCount(2)
//
//        organizationDO = new OrganizationDO()
//        organizationDO.setId(1)
//        organizationDO.setCode("org")
//        organizationDO.setName("测试组织")
//        organizationDO.setEnabled(true)
//        organizationDO.setProjectCount(2)
//
//        projectE = new ProjectE()
//        projectE.setId(1L)
//        projectE.setEnabled(true)
//        projectE.setName("93ecb0-8")
//        projectE.setCode("4c93c5a0-8")
//        projectE.setOrganization(organizationE);
//
//        iamServiceClient = Mock(IamServiceClient)
//        Field field=iamRepository.getClass().getDeclaredFields()[0];
//        field.setAccessible(true)
//        field.set(iamRepository,iamServiceClient)
//
//        wikiSpaceMapper = Mock(WikiSpaceMapper)
//        Field WikiSpaceMapperField = wikiSpaceRepositoryImpl.getClass().getDeclaredFields()[0];
//        WikiSpaceMapperField.setAccessible(true)
//        WikiSpaceMapperField.set(wikiSpaceRepositoryImpl,wikiSpaceMapper)
//    }
//
//    def '扫描组织和项目'() {
//        given: '定义请求数据格式'
//        Page<OrganizationE> pageByOrganization = new Page<>();
//        pageByOrganization.setTotalPages(1)
//        pageByOrganization.setContent(Arrays.asList(organizationE))
//
//        List<WikiSpaceE> wikiSpaceEList = new ArrayList<>()
//        WikiSpaceE wikiSpaceE = new WikiSpaceE();
//        wikiSpaceE.setId(1)
//        wikiSpaceE.setName("O-客户演示")
//        wikiSpaceE.setResourceId(1)
//        wikiSpaceEList.add(wikiSpaceE);
//
//        Page<ProjectE> projectEPage = new Page<>()
//        projectEPage.setTotalPages(1)
//        projectEPage.setContent(Arrays.asList(projectE))
//
//        Page<OrganizationDO> page = new Page<>();
//        page.setContent(Arrays.asList(organizationDO))
//        page.setTotalPages(2)
//        ResponseEntity<Page<OrganizationDO>> responseEntity = new ResponseEntity<>(page, HttpStatus.OK)
//        ResponseEntity<Page<OrganizationDO>> responseEntity1 = new ResponseEntity<>(page, HttpStatus.OK)
//
//        and: 'Mock'
//        1 * iamServiceClient.pageByOrganization(_,_) >> responseEntity
//        1 * iamServiceClient.pageByOrganization(_,_) >> responseEntity1
//        1 * wikiSpaceMapper.select(_) >> null
//
////        1 * iamRepository.pageByOrganization(_,_) >> pageByOrganization
////        2 * wikiSpaceRepository.getWikiSpaceList(*_) >> wikiSpaceEList
////        1 * iamRepository.pageByProject(*_) >> projectEPage
////        1 * wikiSpaceService.create(*_)
////        2 * iWikiUserService.checkDocExsist(_, _) >>> false >> true
////        2 * iWikiGroupService.createGroup(_, _)
////        2 * iWikiGroupService.addRightsToOrg(_, _, _, _)
////        1 * iamRepository.queryUserById(_) >> userE
////        1 * iWikiUserService.checkDocExsist(_, _) >> false
////        1 * iWikiUserService.createUser(_, _, _, _)
////        1 * iWikiGroupService.createGroupUsers(_, _, _)
////        2 * iWikiUserService.checkDocExsist(_, _) >>> false >> true
//
//        when: '模拟发送消息'
//        def entity = restTemplate.getForEntity("/v1/scan",ResponseEntity)
//
//        then: '校验返回数据'
//        Assert.assertEquals(200, entity.statusCodeValue);
//    }
//}
