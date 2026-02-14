import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { Routes, Route, Outlet, BrowserRouter } from "react-router-dom";
import Navbar from './Components/common/Navbar';
import Footer from './Components/common/Footer';
import About from './Components/pages/About';

function PublicLayout() {
  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <Outlet />
      <Footer />
    </div>
  );
}



function App() {


  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<PublicLayout />}>
          <Route path="/about" element={<About />} />
          
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
