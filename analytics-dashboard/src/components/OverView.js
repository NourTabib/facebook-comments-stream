import React from 'react'
import SentimentAnalysisOverview from './SentimentAnalysisOverview'
import WordsCloudOverview from './WordsCloudOverview'
import { useNavigate } from 'react-router-dom';

const OverView = () => {
  return (
    <div>
        <div  style={{
      display:'flex',
      justifyContent:'space-around',
      marginTop:50,
      marginLeft:150,
      
  }}>
        <SentimentAnalysisOverview onClick={()=>console.log("a")} />
        <WordsCloudOverview onClick={()=>console.log("a")} />     
    </div>
    </div>
  )
}

export default OverView