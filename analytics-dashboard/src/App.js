import React from 'react'
import NavBar from './components/NavBar'
import SentimentAnalysisOverview from './components/SentimentAnalysisOverview'
import WordsCloudOverview from './components/WordsCloudOverview'
import { BrowserRouter, Link, Navigate, Route, Routes, useNavigate } from 'react-router-dom';
import OverView from './components/OverView';
import SentimentAnalysis from './components/SentimentAnalysis';
import WordsCloud from './components/WordsCloud';

const App = () => {
  return (
    <div className='App' style={{
      display: 'flex',
      mingHeight: "100vh"
    }}>
      <NavBar />
      <BrowserRouter>
        <Routes>
          <Route  path='/' element={<Navigate to="/overview" replace /> } ></Route>
          <Route index path="/overview" element={<OverView/>}/>
          <Route path="/sentiment-analysis" element={<SentimentAnalysis  />}/>
          <Route path="/words-cloud" element={<WordsCloud  />}/>
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App