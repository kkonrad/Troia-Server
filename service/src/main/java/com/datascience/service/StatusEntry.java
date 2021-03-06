package com.datascience.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.datascience.datastoring.jobs.JobsManager;
import com.datascience.executor.ICommandStatusesContainer;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sun.jersey.spi.resource.Singleton;

@Path("/status/")
@Singleton
public class StatusEntry {

	@Context ServletContext context;
	

	private DateTime getInitializationTimestamp(){
		return (DateTime) context.getAttribute(Constants.DEPLOY_TIME);
	}

	private ResponseBuilder getResponseBuilder(){
		return (ResponseBuilder) context.getAttribute(Constants.RESPONSER);
	}

	private JobsManager getJobsManager(){
		return (JobsManager) context.getAttribute(Constants.JOBS_MANAGER);
	}

	private ICommandStatusesContainer getCommandStatusesContainer(){
		return (ICommandStatusesContainer) context.getAttribute(Constants.COMMAND_STATUSES_CONTAINER);
	}

	@GET
	public Response status(){
		if (!InitializationSupport.checkIsInitialized(context))
			return InitializationSupport.makeNotInitializedResponse(context);
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("status", "OK");
		content.put("deploy_time", getInitializationTimestamp().toString());
		content.put("job_storage", getJobsManager().toString());
		content.put("job_storage_status", getJobStorageStatus());
		content.put("statuses_container", getCommandStatusesContainer().toString());
		content.put("memory", getMemoryStats());
		return getResponseBuilder().makeOKResponse(content);
	}

	protected String getJobStorageStatus() {
		try {
			getJobsManager().test();
			return "OK";
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("JOB STORAGE STATUS FAILED", e);
			return "FAIL: " + e.getMessage();
		}
	}

	protected Map<String, Object> getMemoryStats(){
		Map<String, Object> memory = new HashMap<String, Object>();
		Runtime runtime = Runtime.getRuntime();
		memory.put("free", runtime.freeMemory());
		memory.put("total", runtime.totalMemory());
		memory.put("max", runtime.maxMemory());
		memory.put("used", runtime.totalMemory() - runtime.freeMemory());
		return memory;
	}
}
