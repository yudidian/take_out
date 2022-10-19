import{b as se,q as ne,k as ue,r as i,u as de,v as ce,f as s,o as c,c as g,g as a,w as o,F as k,a as D,n as U,j as m,t as ie,m as V,h as N,E as x}from"./index.733e81c8.js";import{a as A,b as re,u as me,c as _e,e as pe}from"./setMeal.e15aa232.js";import{p as fe}from"./plus.f18ea452.js";const ve={class:"el-form-item-first"},ge=m(" +\u6DFB\u52A0\u83DC\u54C1 "),he=m(" \u5220\u9664 "),be=["src"],ye=m(" \u53D6\u6D88 "),ke=m(" \u786E\u5B9A "),Ve={class:"dialog-footer"},xe=m("\u53D6\u6D88"),we=m("\u786E\u5B9A"),Ce={setup(Ie){const L=ne(),w=ue(),h=i([]),r=i([]),_=i([]),C=i([]),b=i(""),S=de(),I=i(""),n=i({image:"",description:"",price:100,name:"",categoryId:"",setmealDishes:[]}),f=i(!1);ce(()=>{E(),w.query.id&&J(w.query.id)});const $=async()=>{let l;w.query.id?l=await me(n.value):l=await _e(n.value),l.code===1?(L.back(),x.success({message:l.msg})):x.error({message:l.msg})},E=()=>{Promise.all([A(1),A(2)]).then(l=>{_.value=l[0].info,C.value=l[1].info})},T=l=>{n.value.image=l.msg,b.value=`http://localhost:8080/download?fileName=${l.msg}`},j=l=>(console.log(l.type),["image/png","image/jpeg"].findIndex(d=>d===l.type)===-1?(x.error("\u8BF7\u4E0A\u4F20\u56FE\u7247"),!1):l.size/1024/1024>2?(x.error("\u56FE\u7247\u6700\u5927\u4E3A2MB!"),!1):!0),z=async()=>{f.value=!0,await B(_.value[0].id,0)},H=l=>{I.value=C.value.find(e=>e.id===l).name,n.value.categoryId=l},R=(l,e)=>{e.dishId=e.id,delete e.id,e.copies=1,l?r.value.push(e):r.value.splice(r.value.findIndex(u=>u===e),1)},P=()=>{r.value=[],h.value=[],n.value.setmealDishes=[],f.value=!1},F=()=>{n.value.setmealDishes=r.value,f.value=!1},B=async(l,e)=>{const u=await pe({categoryId:l});_.value[e].checkList=u.info},G=async l=>{_.value[l].checkList||await B(_.value[l].id,l)},J=async l=>{const e=await re(l);e.code===1&&(n.value=e.info,r.value=e.info.setmealDishes,h.value=e.info.setmealDishes.map(u=>u.name),I.value=e.info.categoryName,b.value=`http://localhost:8080/download?fileName=${e.info.image}`)};return(l,e)=>{const u=s("el-input"),d=s("el-form-item"),K=s("el-option"),O=s("el-select"),p=s("el-button"),y=s("el-table-column"),M=s("el-input-number"),Q=s("el-table"),W=s("el-icon"),X=s("el-upload"),Y=s("el-form"),Z=s("el-checkbox"),ee=s("el-checkbox-group"),le=s("el-tab-pane"),ae=s("el-tabs"),oe=s("el-dialog");return c(),g(k,null,[a(Y,{model:n.value,"label-position":"left"},{default:o(()=>[D("div",ve,[a(d,{label:"\u83DC\u54C1\u540D\u79F0",class:"is-required"},{default:o(()=>[a(u,{modelValue:n.value.name,"onUpdate:modelValue":e[0]||(e[0]=t=>n.value.name=t),autocomplete:"off"},null,8,["modelValue"])]),_:1}),a(d,{label:"\u83DC\u54C1\u5206\u7C7B",class:"is-required"},{default:o(()=>[a(O,{"model-value":I.value,placeholder:"\u8BF7\u9009\u62E9\u83DC\u54C1\u5206\u7C7B",onChange:H},{default:o(()=>[(c(!0),g(k,null,U(C.value,t=>(c(),V(K,{key:t.id,label:t.name,value:t.id},null,8,["label","value"]))),128))]),_:1},8,["model-value"])]),_:1})]),D("div",null,[a(d,{label:"\u6DFB\u52A0\u5957\u9910\u83DC\u54C1\u79CD\u7C7B",class:"is-required item-dish"},{default:o(()=>[a(p,{type:"primary",onClick:z},{default:o(()=>[ge]),_:1}),a(Q,{data:r.value},{default:o(()=>[a(y,{label:"\u540D\u79F0",width:"120",prop:"name"}),a(y,{label:"\u539F\u4EF7",width:"120",prop:"price"},{default:o(t=>[m(" \uFFE5 "+ie(t.row.price/100),1)]),_:1}),a(y,{label:"\u4EFD\u6570",width:"220",align:"center"},{default:o(t=>[a(M,{min:1,max:10,modelValue:t.row.copies,"onUpdate:modelValue":q=>t.row.copies=q},null,8,["modelValue","onUpdate:modelValue"])]),_:1}),a(y,{label:"\u64CD\u4F5C"},{default:o(()=>[a(p,{type:"danger",text:"",size:"small",color:"#ff8686"},{default:o(()=>[he]),_:1})]),_:1})]),_:1},8,["data"])]),_:1})]),a(d,{label:"\u5957\u9910\u4EF7\u683C",class:"is-required"},{default:o(()=>[a(M,{modelValue:n.value.price,"onUpdate:modelValue":e[1]||(e[1]=t=>n.value.price=t),min:100,max:1e5},null,8,["modelValue"])]),_:1}),a(d,{label:"\u83DC\u54C1\u56FE\u7247",class:"flavor-image is-required"},{default:o(()=>[a(X,{class:"avatar-uploader",action:"api/upload",headers:{token:N(S).getters.token},"show-file-list":!1,"on-success":T,"before-upload":j},{default:o(()=>[b.value?(c(),g("img",{key:0,src:b.value,class:"avatar"},null,8,be)):(c(),V(W,{key:1,class:"avatar-uploader-icon"},{default:o(()=>[a(N(fe))]),_:1}))]),_:1},8,["headers"])]),_:1}),a(d,{label:"\u83DC\u54C1\u63CF\u8FF0",class:"flavor-textArea is-required"},{default:o(()=>[a(u,{modelValue:n.value.description,"onUpdate:modelValue":e[2]||(e[2]=t=>n.value.description=t),maxlength:"200",placeholder:"\u8BF7\u8F93\u5165\u83DC\u54C1\u63CF\u8FF0","show-word-limit":"",type:"textarea"},null,8,["modelValue"])]),_:1}),a(d,{class:"footer"},{default:o(()=>[a(p,{onClick:e[3]||(e[3]=t=>l.$router.back())},{default:o(()=>[ye]),_:1}),a(p,{type:"primary",onClick:$},{default:o(()=>[ke]),_:1})]),_:1})]),_:1},8,["model"]),a(oe,{modelValue:f.value,"onUpdate:modelValue":e[5]||(e[5]=t=>f.value=t),title:"\u9009\u62E9\u83DC\u54C1"},{footer:o(()=>[D("span",Ve,[a(p,{onClick:P},{default:o(()=>[xe]),_:1}),a(p,{type:"primary",onClick:F},{default:o(()=>[we]),_:1})])]),default:o(()=>[a(ae,{"tab-position":"left",style:{height:"200px"},class:"demo-tabs",onTabChange:G},{default:o(()=>[(c(!0),g(k,null,U(_.value,(t,q)=>(c(),V(le,{label:t.name,key:q},{default:o(()=>[a(ee,{modelValue:h.value,"onUpdate:modelValue":e[4]||(e[4]=v=>h.value=v)},{default:o(()=>[(c(!0),g(k,null,U(t.checkList,v=>(c(),V(Z,{label:v.name,onChange:te=>R(te,v),key:v.id},null,8,["label","onChange"]))),128))]),_:2},1032,["modelValue"])]),_:2},1032,["label"]))),128))]),_:1})]),_:1},8,["modelValue"])],64)}}};var Be=se(Ce,[["__scopeId","data-v-1e7c5887"]]);export{Be as default};
