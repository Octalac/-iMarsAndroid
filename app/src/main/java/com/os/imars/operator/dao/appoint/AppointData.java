package com.os.imars.operator.dao.appoint;

import com.google.gson.annotations.SerializedName;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.operator.dao.vessel.VesselData;

import java.io.Serializable;
import java.util.List;

public class AppointData implements Serializable {

    @SerializedName("type_data")
    private List<CategoryDataItem> categoryData;

    @SerializedName("port_data")
    private List<PortDataItem> portData;

    @SerializedName("vessels")
    private List<VesselData> vesselData;

    @SerializedName("agents")
    private List<AgentsDataItem> agentData;

    public List<VesselData> getVesselData() {
        return vesselData;
    }

    public void setVesselData(List<VesselData> vesselData) {
        this.vesselData = vesselData;
    }



    public List<AgentsDataItem> getAgentData() {
        return agentData;
    }

    public void setAgentData(List<AgentsDataItem> agentData) {
        this.agentData = agentData;
    }



    public void setCategoryData(List<CategoryDataItem> categoryData) {
        this.categoryData = categoryData;
    }

    public List<CategoryDataItem> getCategoryData() {
        return categoryData;
    }

    public void setPortData(List<PortDataItem> portData) {
        this.portData = portData;
    }

    public List<PortDataItem> getPortData() {
        return portData;
    }

}


