import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { Routes, Route, Outlet, BrowserRouter } from "react-router-dom";
import Navbar from './Components/common/Navbar';
import Footer from './Components/common/Footer';
import HomePage from "./Components/pages/Home";
import About from './Components/pages/About';
import Service from "./Components/pages/Service";
import Contact from "./Components/pages/Contact";
import Signin from "./Components/pages/Signin";
import UserAccountApi from './services/UserAccountApi';
import Authentication from './services/Authentication';
import { FaSignInAlt } from 'react-icons/fa';
import Signup from "./Components/pages/Signup";


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
         <Route path="/" element={<HomePage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/about" element={<About />} />
          <Route path="/service" element={<Service />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/sign-in" element={<Signin />} />
          <Route path="/sign-up" element={<Signup />} />
          
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App