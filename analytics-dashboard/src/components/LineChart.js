import React , { useEffect, useState }from 'react'
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
ChartJS.register(LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
  zoomPlugin
  );
const LineChart = (props) => {
  return (
    <>
      {
        props.data ? (
          <Line options={props.options} data={props.data}></Line>
        ):null
      }
    </>
  )
}

export default LineChart