import '../App.css';
import React, { useEffect, useState } from 'react';
import zoomPlugin from 'chartjs-plugin-zoom';
import {Line} from 'react-chartjs-2';
import {
   Chart as ChartJS,
   LineElement,
   CategoryScale,
   LinearScale,
   PointElement,
   registerables 
} from 'chart.js';
import { Chart } from 'react-chartjs-2'
import axios from 'axios';
ChartJS.register(LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  zoomPlugin
  );

function App() {
  const [records, setRecords] = useState({})
  const [recordsDay, setRecordsDay] = useState({})
  function filterLast24Hours(enrichedComments) {
    // Get the current date and time
    const currentDate = new Date();
  
    // Calculate the date and time exactly 24 hours ago
    const last24Hours = new Date();
    last24Hours.setHours(currentDate.getHours() - 24);
  
    // Filter the enriched comments array based on the timestamp
    const filteredComments = enrichedComments.filter(comment => {
      // Convert the comment's timestamp to a Date object
      const commentTimestamp = new Date(comment.timestamps);
  
      // Return true if the comment's timestamp is greater than the last 24 hours
      return commentTimestamp > last24Hours;
    });
  
    return filteredComments;
  }
  useEffect(()=> {
    const all = async ()=>{axios.get("http://localhost:8080/users/test").then(msg => {setRecords(msg.data); console.log(msg.data);console.log(Object.keys(records)["52"])})}
    const day = async ()=>{axios.get("http://localhost:8080/users/test3").then(msg => {console.log(msg.data);setRecordsDay(msg.data)})}
    try{all();day()}catch(e){console.log(e)}
  },[])
  try{
    const getKeys = (listOfRecords) => {
      return Object.keys(listOfRecords)
    }
    const getPosData = (listOfRecords) => {
      return keys.map(key => listOfRecords[key].filter(el => el.sentiment >= 0.5))
    }
    const getNegData = (listOfRecords) => {
      return keys.map(key => listOfRecords[key].filter(el => el.sentiment <= 0.5))
    }
    var keys = Object.keys(records)
    var keysDay = Object.keys(recordsDay)
    var posData = keys.map(key => records[key].filter(el => el.sentiment >= 0.5))
    var negData = keys.map(key => records[key].filter(el => el.sentiment <= 0.5))
    var dayNegData = keysDay.map(key => recordsDay[key].filter(el => el.sentiment <= 0.5))
    var dayPosData = keysDay.map(key => recordsDay[key].filter(el => el.sentiment >= 0.5))
    const data = {
      labels : keys,
      datasets : [{
        data: negData.map(data => data.length),
        backgroundColor:'transparent',
        borderColor : 'red',
        pointBorderColor: 'transparent',
        pointBorderWidth : 4
      },
      {
        data: posData.map(data => data.length),
        backgroundColor:'transparent',
        borderColor : 'blue',
        pointBorderColor: 'transparent',
        pointBorderWidth : 4
      },]
    }
    const options = {
      plugins: {
        zoom: {
          zoom: {
            wheel: {
              enabled: true,
            },
            pinch: {
              enabled: true
            },
            mode: 'xy',
          }
        }
      }
    }
    const dataDay = {
      labels : keysDay,
      datasets : [{
        data: dayNegData.map(data => data.length),
        backgroundColor:'transparent',
        borderColor : 'red',
        pointBorderColor: 'transparent',
        pointBorderWidth : 4
      },
      {
        data: dayPosData.map(data => data.length),
        backgroundColor:'transparent',
        borderColor : 'blue',
        pointBorderColor: 'transparent',
        pointBorderWidth : 4
      },]
    }
    const optionsDay = {
      plugins: {
        zoom: {
          zoom: {
            wheel: {
              enabled: true,
            },
            pinch: {
              enabled: true
            },
            mode: 'xy',
          }
        }
      }
    }
    return (
      
      <div className="App">
        <div className="App">
        <div>
          <Line options={optionsDay} data={dataDay}></Line>
        </div>
      </div>
      <div className="App">
        <div>
          <Line options={options} data={data}></Line>
        </div>
      </div>
      </div>
    );
  }catch(e){
    console.log(e)
  }
}

export default App;
