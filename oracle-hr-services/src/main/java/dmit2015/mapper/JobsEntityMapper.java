package dmit2015.mapper;

import dmit2015.dto.JobsEntityDto;
import dmit2015.entity.JobsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobsEntityMapper {

    JobsEntityMapper INSTANCE = Mappers.getMapper(JobsEntityMapper.class);

    JobsEntityDto toDto(JobsEntity entity);

    JobsEntity toEntity(JobsEntityDto dto);

}

