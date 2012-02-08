// IMPORTS
// add dependencies here
/**  * Type:org.unidal.esf.doc.bit.sample.BitByBit
*/
vjo.ctype("bitbybit.BitByBit")
.protos({
	
	/**
	 * @JsReturnType void
	 * @JsJavaAccessToJs public
	*/
    //> public constructs()
	constructs:function(){
	},
		
	/**
	 * @JsJavaAccessToJs public
	 * @JsEventHandler
	 * @JsParamType pName String
	*/
    //> public void click(String pName)
	click:function(pName){
		alert("Button clicked" + pName);
	}
})
.endType();
