package org.radarcns.management.service.dto;


import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the Source entity.
 */
public class SourceDTO implements Serializable {

    private Long id;

    @NotNull
    private String sourceId;

    private String deviceCategory;

    @NotNull
    private Boolean assigned;

    private DeviceTypeDTO deviceType;

    private MinimalProjectDetailsDTO project;

    private Map<String, String> attributes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }
    public Boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(Boolean assigned) {
        this.assigned = assigned;
    }

    public DeviceTypeDTO getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeDTO deviceType) {
        this.deviceType= deviceType;
    }

    public MinimalProjectDetailsDTO getProject() {
        return project;
    }

    public void setProject(MinimalProjectDetailsDTO project) {
        this.project = project;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SourceDTO sourceDTO = (SourceDTO) o;

        if ( ! Objects.equals(id, sourceDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SourceDTO{" +
            "id=" + id +
            ", sourceId='" + sourceId + "'" +
            ", deviceCategory='" + deviceCategory + "'" +
            ", activated='" + assigned + "'" +
            '}';
    }
}
