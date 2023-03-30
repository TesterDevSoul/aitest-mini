package com.ceshiren.aitestmini.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Repository;
import com.ceshiren.aitestmini.dto.TestCaseDTO;
import com.ceshiren.aitestmini.entity.TestCase;

import java.util.List;


//生成的映射器是一个单例范围的 Spring bean，可以通过以下方式检索@Autowired
@Mapper(componentModel = "spring")
@Repository
public interface TestCaseConverter {
    @Mappings({
            @Mapping(target = "caseName",source = "caseName"),
            @Mapping(target = "caseData",source = "caseData"),
            @Mapping(target = "remark",source = "remark")
    })
    TestCase testCaseDTOForTestCase(TestCaseDTO testCaseDTO);

    @Mappings({
            @Mapping(target = "caseName",source = "caseName"),
            @Mapping(target = "caseData",source = "caseData"),
            @Mapping(target = "remark",source = "remark")
    })
    TestCaseDTO testCaseForTestCaseDTO(TestCase testCase);

    List<TestCaseDTO> testCaseListForTestCaseDTOList(List<TestCase> testCaseList);
}
