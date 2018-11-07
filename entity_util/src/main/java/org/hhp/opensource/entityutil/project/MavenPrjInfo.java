package org.hhp.opensource.entityutil.project;

public class MavenPrjInfo {
	
	private String groupId;
	
	private String artifactId;
	
	private String version;
	
	private PackagingEnum packaging;
	
	private String name;
	
	private String description;
	
	private String partentPrjName;
	
	private String parentPrjArtifactId;
	
	private String patentPrjVersion;

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

	public String getPartentPrjName() {
		return partentPrjName;
	}

	public void setPartentPrjName(String partentPrjName) {
		this.partentPrjName = partentPrjName;
	}

	public String getParentPrjArtifactId() {
		return parentPrjArtifactId;
	}

	public void setParentPrjArtifactId(String parentPrjArtifactId) {
		this.parentPrjArtifactId = parentPrjArtifactId;
	}

	public String getPatentPrjVersion() {
		return patentPrjVersion;
	}

	public void setPatentPrjVersion(String patentPrjVersion) {
		this.patentPrjVersion = patentPrjVersion;
	}
	
}
