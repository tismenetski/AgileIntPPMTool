package com.tismenetski.ppmtool.services;

import com.tismenetski.ppmtool.domain.Backlog;
import com.tismenetski.ppmtool.domain.Project;
import com.tismenetski.ppmtool.domain.ProjectTask;
import com.tismenetski.ppmtool.exceptions.ProjectNotFoundException;
import com.tismenetski.ppmtool.repositories.BacklogRepository;
import com.tismenetski.ppmtool.repositories.ProjectRepository;
import com.tismenetski.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {


    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;


    public ProjectTask addProjectTask(String projectIdentifier , ProjectTask projectTask)
    {

        try
        {
            //Exceptions: Project not found
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier); //get the backlog from the database using the given projectIdentifier

            projectTask.setBacklog(backlog); //set the backlog the the project task
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            //Add sequence to project task
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if (projectTask.getPriority()==null)
            {
                projectTask.setPriority(3);
            }
            if (projectTask.getStatus()=="" || projectTask.getStatus()==null)
            {
                projectTask.setStatus("To_DO");
            }
            return projectTaskRepository.save(projectTask);
        }
        catch (Exception e)
        {
            throw new ProjectNotFoundException("Project not Found");
        }


        //PTs to be added to a specific project, project !=null , BL exists
        //set the backlog to the project task
        //we want our project sequence to be like this : IDPRO-1 IDPRO-2 ....
        //Update the BL sequence
        //INITIAL priority when priority is null
        //INITIAL status when status is null
    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);
        if (project==null) throw new ProjectNotFoundException("Project with ID "+ id +" Does Not Exist");
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);

    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id)
    {
        //make sure we are searching on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null) throw new ProjectNotFoundException("Project with ID "+ backlog_id +" Does Not Exist");

        //make sure our task exist
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null) throw  new ProjectNotFoundException("Project Task  "+ pt_id +" Not Found");

        //make sure that backlog/project id in the path corresponds to the right project
        if (!projectTask.getProjectIdentifier().equals(backlog_id))
            throw new ProjectNotFoundException("Project Task "+ pt_id+ " does not exist in project: "+ backlog_id);

        return projectTask;
    }
}
