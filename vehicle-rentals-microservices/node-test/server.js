const express=require("express")

const app=express();



app.get("/",(req,res)=>{
    res.send("Hello. from Nodejs");
})

app.get("/node",(req,res)=>{
    res.send("Hey from Node");
})

app.listen(3000,()=>{
    console.log("Server is running at http://135.235.216.179:3000");
})


require("./eureja")
