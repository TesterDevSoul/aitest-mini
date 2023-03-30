package com.ceshiren.aitestmini.converter;

import com.ceshiren.aitestmini.dto.task.TestTaskAddDTO;
import com.ceshiren.aitestmini.dto.task.TestTaskDTO;
import com.ceshiren.aitestmini.entity.TestTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Repository;

//生成的映射器是一个单例范围的 Spring bean，可以通过以下方式检索@Autowired
@Mapper(componentModel = "spring")
@Repository
public interface TestTaskConverter {
    //target：TestTask   source：TestTaskAddDTO
    @Mappings({
            @Mapping(target = "name",source = "taskName"),
            @Mapping(target = "remark",source = "remark")
    })
    TestTask testTaskAddDtoForTestTask(TestTaskAddDTO testTaskDTO);

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "name",source = "taskName"),
            @Mapping(target = "buildUrl",source = "buildUrl"),
            @Mapping(target = "status",source = "status"),
            @Mapping(target = "testCommand",source = "command"),
            @Mapping(target = "remark",source = "remark")
    })
    TestTask testTaskDtoForTestTask(TestTaskDTO testTaskDTO);

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "taskName",source = "name"),
            @Mapping(target = "buildUrl",source = "buildUrl"),
            @Mapping(target = "status",source = "status"),
            @Mapping(target = "command",source = "testCommand"),

            @Mapping(target = "remark",source = "remark")
    })
    TestTaskDTO testTaskForTestTaskDto(TestTask testTask);
}
