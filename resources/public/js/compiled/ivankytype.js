if(typeof Math.imul == "undefined" || (Math.imul(0xffffffff,5) == 0)) {
    Math.imul = function (a, b) {
        var ah  = (a >>> 16) & 0xffff;
        var al = a & 0xffff;
        var bh  = (b >>> 16) & 0xffff;
        var bl = b & 0xffff;
        // the shift by 0 fixes the sign on the high part
        // the final |0 converts the unsigned value into a signed value
        return ((al * bl) + (((ah * bl + al * bh) << 16) >>> 0)|0);
    }
}


;(function(){
/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
            ;function d(a){const b=a.length;if(0<b){const f=Array(b);for(let c=0;c<b;c++)f[c]=a[c];return f}return[]};var e={},g={};if("undefined"===typeof e||"undefined"===typeof g||"undefined"===typeof h)var h={};if("undefined"===typeof e||"undefined"===typeof g||"undefined"===typeof k)var k=null;if("undefined"===typeof e||"undefined"===typeof g||"undefined"===typeof l)var l=null;if("undefined"===typeof e||"undefined"===typeof g||"undefined"===typeof m)var m=null;
var n="undefined"!==typeof Math&&"undefined"!==typeof Math.imul?function(a,b){return Math.imul(a,b)}:function(a,b){var f=a&65535,c=b&65535;return f*c+((a>>>16&65535)*c+f*(b>>>16&65535)<<16>>>0)|0};function p(a){a=n(a|0,-862048943);a=0^(n(a<<15|a>>>-15,461845907)|0);a=(n(a<<13|a>>>-13,5)+-430675100|0)^0;a=n(a^a>>>16,-2048144789);n(a^a>>>13,-1028477387)}p(1);p(0);if("undefined"===typeof e||"undefined"===typeof g||"undefined"===typeof q)var q=null;
"undefined"!==typeof console&&(k=function(){var a=arguments;return console.log.apply(console,d.call(null,a))},l=function(){var a=arguments;return console.error.apply(console,d.call(null,a))});if("undefined"===typeof e||"undefined"===typeof g||"undefined"===typeof r)var r=function(){throw Error("cljs.core/*eval* not bound");};
})();
