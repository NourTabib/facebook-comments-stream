import React, { useEffect, useState } from 'react';
import DoughnutChart from './DoughnutChart';
import axios from 'axios';
import randomColor from 'randomcolor';
import { useNavigate } from 'react-router-dom';
const WordsCloudOverview = () => {
  const navigate = useNavigate();
  const [records, setRecords] = useState([])
  useEffect(() => {
    const getData = async () =>{
      await axios.get("http://localhost:8080/wc/year").then(res => {console.log(res.data);setRecords(res.data)})
    }
    try{
      getData();
    }catch(e){
      console.log(e);
    }
  },[])
  const options = {
    plugins:{legend: {
      display: true,
      position: "right"
    },
    elements: {
      arc: {
        borderWidth: 0
      }
    }}
  };
  const pieOptions = {
    plugins:{legend: {
      display: true,
      position: "right",
    },
    elements: {
      arc: {
        borderWidth: 0
      }
    }}
  };
  var keys = records.filter(record => record.total >= 2000).map(record => record.word);
  var dataset = records.map(record => record.total).filter(count => count >= 2000);
  var data = {
    maintainAspectRatio: false,
    responsive: false,
    labels: keys,
    datasets: [{
      label: '',
      data: dataset,
      backgroundColor: randomColor({ count: 6 }),
      hoverOffset: 4
    }]
  }
  return (
    <div 
    onClick={()=>{
      navigate('/words-cloud')
    }}
    style={{
      marginLeft:50,
      height:500,
      width:800,
      boxShadow : "0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)",
      borderRadius: 20,
    }}>
      <h3 style={{
        marginTop:18,
        marginLeft:15
      }} >Trending Words Overview</h3>  
      <DoughnutChart pieOptions={pieOptions} options={options} data={data} />
    </div>
  )
}

export default WordsCloudOverview