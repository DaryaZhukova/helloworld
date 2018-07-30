package hudson.plugins.ws_cleanup;
import jenkins.model.Jenkins;
import hudson.model.FreeStyleProject;
import hudson.tasks.Shell;
import hudson.plugins.git.*;
def repository = 'https://github.com/DaryaZhukova/helloworld.git'
//create job
job = Jenkins.instance.createProject(FreeStyleProject, 'test')
//add github repository
job.scm = new hudson.plugins.git.GitSCM(repository)
job.scm.branches[0].name = '*/master'
//add shell step
job.buildersList.add(new Shell('echo hello world'))
//cleanup workspace
job.buildWrappersList.add(new PreBuildCleanup(null, true, "", ""))
job.save()
