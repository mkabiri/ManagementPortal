package org.radarcns.management.service.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.radarcns.management.domain.Source;
import org.radarcns.management.service.dto.MinimalSourceDetailsDTO;
import org.radarcns.management.service.dto.SourceDTO;
import org.radarcns.management.service.mapper.decorator.SourceMapperDecorator;

import java.util.List;

/**
 * Mapper for the entity Source and its DTO SourceDTO.
 */
@Mapper(componentModel = "spring", uses = {SourceTypeMapper.class, ProjectMapper.class})
@DecoratedWith(SourceMapperDecorator.class)
public interface SourceMapper {

    @Mapping(source = "source.subject.user.login", target = "subjectLogin")
    SourceDTO sourceToSourceDTO(Source source);

    @Mapping(source = "sourceType.id", target = "sourceTypeId")
    @Mapping(source = "sourceType.producer", target = "sourceTypeProducer")
    @Mapping(source = "sourceType.model", target = "sourceTypeModel")
    @Mapping(source = "sourceType.catalogVersion", target = "sourceTypeCatalogVersion")
    @Mapping(source = "assigned" , target = "assigned")
    MinimalSourceDetailsDTO sourceToMinimalSourceDetailsDTO(Source source);

    List<MinimalSourceDetailsDTO> sourcesToMinimalSourceDetailsDTOs(List<Source> sources);

    @Mapping(target = "sourceType", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "subject", ignore = true)
    Source descriptiveDTOToSource(MinimalSourceDetailsDTO minimalSourceDetailsDto);

    List<SourceDTO> sourcesToSourceDTOs(List<Source> sources);

    @Mapping(target = "subject", ignore = true)
    Source sourceDTOToSource(SourceDTO sourceDto);
}
