package cn.e3mall.pojo;


/*
 * 商品规格参数与商品类目关系
 */
public class ItemParamWithName extends TbItemParam {
	
	private static final long serialVersionUID = 1L;
	
	private String itemCatName;

	
	public ItemParamWithName() {
		
	}

	public ItemParamWithName(TbItemParam itemParam,String itemCatName) {
		
		this.itemCatName = itemCatName;
		this.setCreated(itemParam.getCreated());
		this.setId(itemParam.getId());
		this.setItemCatId(itemParam.getItemCatId());
		this.setParamData(itemParam.getParamData());
		this.setUpdated(itemParam.getUpdated());
	}

	public String getItemCatName() {
		return itemCatName;
	}

	public void setItemCatName(String itemCatName) {
		this.itemCatName = itemCatName;
	}

//	@Override
//	public String toString() {
//		return "ItemParamWithName [itemCatName=" + itemCatName + ", getId()=" + getId() + ", getItemCatId()="
//				+ getItemCatId() + ", getCreated()=" + getCreated() + ", getUpdated()=" + getUpdated()
//				+ ", getParamData()=" + getParamData() + "]";
//	}
	
	
	
	

}
