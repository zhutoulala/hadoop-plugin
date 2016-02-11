package hudson.plugins.hadoop;

import java.io.IOException;
import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor.FormException;
import hudson.plugins.hadoop.HDFSDeployPublisher.DescriptorImpl;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;

public class HadoopJobBuilder extends Builder implements Serializable {
	
	private final String jobHDFSPath;
	
	@DataBoundConstructor
	public HadoopJobBuilder(String jobHDFSPath) {
		this.jobHDFSPath = jobHDFSPath;
	}
	
	public String getJobHDFSPath() {
		return jobHDFSPath;
	}
	
	@Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        
		HadoopJob hadoopJob = new HadoopJob(jobHDFSPath, HadoopJob.JobType.MAPREDUCE);
		
        return hadoopJob.run();
    }
	
	@Override
    public DescriptorImpl getDescriptor() {
        // see Descriptor javadoc for more about what a descriptor is.
        return (DescriptorImpl)super.getDescriptor();
    }

    // this annotation tells Hudson that this is the implementation of an extension point
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

    	private String hdfsURI;
    	
        public DescriptorImpl() {
            load();
        }
    	
        @Override
        public String getDisplayName() {
            return Messages.HDFSDeployPublisherName();
        }

        @Override
        public boolean isApplicable(Class type) {
            return true;
        }
        
        @Override
        public boolean configure(StaplerRequest staplerRequest, JSONObject json) throws FormException {
            // to persist global configuration information,
            // set that to properties and call save().
        	hdfsURI = json.getString("HDFSURI");
            save();
            return true; // indicate that everything is good so far
        }

        public String getHDFSURI() {
            return hdfsURI;
        }
    }


}
