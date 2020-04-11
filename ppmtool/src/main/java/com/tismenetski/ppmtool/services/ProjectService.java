package com.tismenetski.ppmtool.services;

import com.tismenetski.ppmtool.domain.Backlog;
import com.tismenetski.ppmtool.domain.Project;
import com.tismenetski.ppmtool.exceptions.ProjectIdException;
import com.tismenetski.ppmtool.repositories.BacklogRepository;
import com.tismenetski.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project)
    {
        try
        {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if (project.getId()==null)
            {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if (project.getId()!=null)
            {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }
        catch (Exception e)
        {
            throw new ProjectIdException("Project ID "+ project.getProjectIdentifier().toUpperCase()+" Already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) throw new ProjectIdException("Project ID  Doesn't exist");
        return project;
    }

    public Iterable<Project> findAllProjects()
    {
        return projectRepository.findAll();
    }

    public void deleteProjectById(String projectId)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) throw new ProjectIdException("Cannot delete Project with ID " +projectId + " .This project does not exist");
        projectRepository.delete(project);
    }
}
