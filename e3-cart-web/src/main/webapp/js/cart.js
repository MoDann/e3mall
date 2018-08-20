var CART = {
	itemNumChange : function(){
		$(".increment").click(function(){//＋
			var _thisInput = $(this).siblings("input");
			_thisInput.val(eval(_thisInput.val()) + 1);
			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				CART.refreshTotalPrice();
				CART.refreshTotal(_thisInput.val(),_thisInput.attr("itemId"));
			});
		});
		$(".decrement").click(function(){//-
			var _thisInput = $(this).siblings("input");
			if(eval(_thisInput.val()) == 1){
				return ;
			}
			_thisInput.val(eval(_thisInput.val()) - 1);
			$.post("/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".action",function(data){
				CART.refreshTotalPrice();
				CART.refreshTotal(_thisInput.val(),_thisInput.attr("itemId"));
			});
		});
		/*$(".itemnum").change(function(){
			var _thisInput = $(this);
			$.post("/service/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val(),function(data){
				CART.refreshTotalPrice();
			});
		});*/
	},
	refreshTotalPrice : function(){ //重新计算总价
		var total = 0;
		$(".itemnum").each(function(i,e){
			var _this = $(e);
			total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
		});
		$("#allMoney2").html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
			 prefix: '¥',
			 thousandsSeparator: ',',
			 centsLimit: 2
		});
	},
	refreshTotal : function(num,id){ //重新计算总价
		var total = 0;
		$(".itemnum").each(function(i,e){
			var _this = $(e);
			if(id == _this.attr("itemId")){
				total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
			}
			$("#"+id).html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
				 prefix: '¥',
				 thousandsSeparator: ',',
				 centsLimit: 2
			});
		});
	}
};


$(function(){
	CART.itemNumChange();
});

function delAll() {
	location.href = "/cart/clearCart.action"
}

function cartDelMore() {
	var sels = $("input:checked");
	var ids = [];
	for(var i=0;i<sels.length;i++){
		ids.push(sels[i].value);
	}
	ids = ids.join(",");
	if(ids.length == 0){
		jAlert("没有选择商品");
		return;
	}
	if(confirm("确定删除选中的商品？")){
		//确定后的操作
		$.post("/cart/cartDelMore.action",{ids:ids},function(data){
			if(data.status == 200){
				jAlert("提示：删除成功",function() {
					$("input:checked".attr("checked",false));
				//	window.location.reload;
				})
			}
		});
	}
}







