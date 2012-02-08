function showTimezoneInSharedBitByBitJs(){
   var date = new Date();
   var timezone = date.getTimezoneOffset()/60 * (-1);
   if(timezone>0){
      alert("Your Timezone: +"+timezone);
   } else {
      alert("Your Timezone: "+timezone);
   }
}
