package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Project;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.List;

@Data
@NoArgsConstructor
public class ProjectResponse extends ComponentResponse {

    private String projectName;

    private Long productionYear;

    private boolean isTeam;

    private String teamMembers;

    private List<String> skills;

    private String projectContent;

    private String projectUrl;

    public ProjectResponse(Project project, Component component) {
        super(component);
        this.projectName = project.getProjectName();
        this.productionYear = project.getProductionYear();
        this.isTeam = !project.getTeamMembers().equals("");
        this.teamMembers = project.getTeamMembers();
        this.skills = project.getSkills();
        this.projectContent = project.getProjectContent();
        this.projectUrl = project.getProjectUrl();
    }

}
