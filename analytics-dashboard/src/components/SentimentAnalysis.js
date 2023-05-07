import React, { useEffect, useState } from 'react'
import LineChart from './LineChart'
import axios from 'axios'
import Button from 'react-bootstrap/Button';
const SentimentAnalysis = () => {
  const [records,setRecords] = useState({})
  const [periode,setPeriod] = useState("year")
  useEffect(() => {
    const getData = async () =>{
      await axios.get("http://localhost:8080/sa/"+periode).then(res => {console.log(res.data);setRecords(res.data)})
    }
    try{
      getData();
    }catch(e){
      console.log(e);
    }
  },[records,periode])
  var keys = Object.keys(records);
  var positiveRecords = keys.map(key => records[key].filter(el => el.sentiment >= 0.5));
  var negativeRecords = keys.map(key => records[key].filter(el => el.sentiment <= 0.5));
  const data = {
    labels : keys,
    datasets : [{
      label:"Negative Comments",
      data: negativeRecords.map(data => data.length),
      backgroundColor:'transparent',
      borderColor : 'red',
      pointBorderColor: 'transparent',
      pointBorderWidth : 4
    },
    {
      label:"Positive Comments",
      data: positiveRecords.map(data => data.length),
      backgroundColor:'transparent',
      borderColor : 'blue',
      pointBorderColor: 'transparent',
      pointBorderWidth : 4
    },]
  }; 
  const options = {
    scales: {
      x: {
         display: true,
         grid: {
          display: true
       }
      },
      y: {
        grid: {
          display: true
       },
         display: false,
         
      }
   },
    plugins:{legend: {
      display: false,
    }}
  };
  return (
    <div style={{
      width:1300,
      height:'100%',
      marginLeft:200,
      marginTop:50
    }}>
      <div style={{
        display:'flex',
        justifyContent:'center',
      alignContent:'center',
      alignItems:'center',
      }}>
        <Button onClick={()=>{
          setPeriod("year")
        }}  style={{
          marginLeft:10
        }}>Year Statistics</Button>
        <Button onClick={()=>{
          setPeriod("day")
        }}  style={{
          marginLeft:10
        }}>Real-time Statistics</Button>
      </div>
      <div style={{
        width:"100%"
      }}>
        <LineChart data={data} options={data} />
      </div>
    </div>
  )
}

export default SentimentAnalysis