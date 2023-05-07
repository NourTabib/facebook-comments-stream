import React, { useEffect, useState } from 'react'
import LineChart from './LineChart'
import axios from 'axios'
import { useNavigate } from 'react-router-dom';

const SentimentAnalysisOverview = () => {

  const navigate = useNavigate();
  const [records, setRecords] = useState({})
  useEffect(() => {
    const getData = async () =>{
      await axios.get("http://localhost:8080/sa/year").then(res => {console.log(res.data);setRecords(res.data)})
    }
    try{
      getData();
    }catch(e){
      console.log(e);
    }
  },[])
  var MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  var keys = Object.keys(records);
  var dataLabels = keys.map(key => MONTHS[key])
  var positiveRecords = keys.map(key => records[key].filter(el => el.sentiment >= 0.5));
  var negativeRecords = keys.map(key => records[key].filter(el => el.sentiment <= 0.5));
  const data = {
    labels : dataLabels,
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
    <div onClick={()=>{
      navigate('/sentiment-analysis')
    }} style={{
      height:500,
      width:800,
      boxShadow : "0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)",
      borderRadius: 20,
    }}>
      <h3 style={{
        marginTop:18,
        marginLeft:15
      }}>Sentiment Analysis Overview</h3>
      <LineChart options={options} data={data}/>
    </div>
  )
}

export default SentimentAnalysisOverview