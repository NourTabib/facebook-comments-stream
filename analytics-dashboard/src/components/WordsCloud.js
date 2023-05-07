import randomColor from 'randomcolor'
import React, { useEffect, useState } from 'react'
import DoughnutChart from './DoughnutChart'
import axios from 'axios'

const WordsCloud = () => {
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
    <div style={{
      marginLeft:250,
    }}>
      <h3>Trending Topics</h3>
      <div style={{
      
      display:'flex',
      justifyContent:'center',
      alignContent:'center',
      alignItems:'center',
      width: "100%",
      height:'100%'

    }}>
      <DoughnutChart pieOptions={pieOptions} options={options} data={data} />
    </div>
    </div>
  )
}

export default WordsCloud