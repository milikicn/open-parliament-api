package rs.otvoreniparlament.api.config;

public class ElasticConfig {

	public String clusterName;
	public int port;
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}