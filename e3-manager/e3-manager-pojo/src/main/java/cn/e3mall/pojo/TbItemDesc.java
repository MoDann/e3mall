package cn.e3mall.pojo;

import java.io.Serializable;
import java.util.Date;

import cn.e3mall.common.util.E3mallResult;

public class TbItemDesc implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long itemId;

    private Date created;

    private Date updated;

    private String itemDesc;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
    }
    
    /**
     * json格式中需要判断状态
     * @return
     */
    public E3mallResult getE3Result() {
        return E3mallResult.ok();
    }
}