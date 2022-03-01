package dmit2015.mapper;

import dmit2015.dto.LocationsEntityDto;
import dmit2015.entity.LocationsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationsEntityMapper {

    LocationsEntityMapper INSTANCE = Mappers.getMapper(LocationsEntityMapper.class);

    LocationsEntityDto toDto(LocationsEntity entity);

    LocationsEntity toEntity(LocationsEntityDto dto);

}

