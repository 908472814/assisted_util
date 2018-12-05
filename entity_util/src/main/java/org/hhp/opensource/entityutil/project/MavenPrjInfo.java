package org.hhp.opensource.entityutil.project;

public class MavenPrjInfo {
	
	private String groupId;
	
	private String artifactId;
	
	private String version;
	
	private PackagingEnum packaging;
	
	private String name;
	
	private String description;
	
	private String parentGroupId = "org.springframework.boot";
	
	private String parentArtifactId = "spring-boot-starter-parent";
	
	private String patentVersion = "2.0.3.RELEASE";

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public PackagingEnum getPackaging() {
		return packaging;
	}

	public void setPackaging(PackagingEnum packaging) {
		this.packaging = packaging;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentGroupId() {
		return parentGroupId;
	}

	public void setParentGroupId(String parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	public String getParentArtifactId() {
		return parentArtifactId;
	}

	public void setParentArtifactId(String parentArtifactId) {
		this.parentArtifactId = parentArtifactId;
	}

	public String getPatentVersion() {
		return patentVersion;
	}

	public void setPatentVersion(String patentVersion) {
		this.patentVersion = patentVersion;
	}
	
}
