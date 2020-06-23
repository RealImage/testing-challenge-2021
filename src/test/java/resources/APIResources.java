package resources;

public enum APIResources {
	PostUploadFile("/sharebox/api/upload"),
	GetParticularFile("/sharebox/api/upload"),
	PutUpdateStatus("/sharebox/api/upload"),
	GetAllFiles("/sharebox/api/files"),
	PostSharingFilesToAnotherUser("/sharebox/api/files"),
	PutAcceptRejectFiles("/sharebox/api/files"),
	DeleteGivenFile("/sharebox/api/files");
	
	private String resource;
	APIResources(String resource)
	{
		this.resource = resource;
	}
	public String getResource()
	{
		return resource;
	}
}
