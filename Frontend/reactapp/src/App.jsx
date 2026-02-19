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
<<<<<<< Updated upstream

=======
import StallMap from './Components/pages/StallMap';
import AdminDashboard from "./Components/Adminpages/AdminDashboard";
import AdminStallPricing from './Components/Adminpages/AdminStallPricing';  
import Adminstalls from './Components/Adminpages/Adminstalls';
import AdminEvents from './Components/Adminpages/AdminEvents';
import AdminMaps from './Components/Adminpages/AdminMaps';
import AdminQrpass from './Components/Adminpages/AdminQrpass';
import AdminReservertion from './Components/Adminpages/AdminReservertion';
import Adminstatistics from './Components/Adminpages/Adminstatistic';
import AdminTransactions from './Components/Adminpages/AdminTransactions';
import AdminUser from './Components/Adminpages/AdminUser';
import AdminPayment from './Components/Adminpages/AdminPayment';
>>>>>>> Stashed changes

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
          
<<<<<<< Updated upstream
=======
          <Route index element={<Adminstatistics />} />
          <Route path="stalls" element={<Adminstalls />} />
          <Route path="stallPricing" element={<AdminStallPricing />} />
          <Route path="reservations" element={<AdminReservertion />} />
          <Route path="events" element={<AdminEvents />} />
          <Route path="maps" element={<AdminMaps />} />
          <Route path="qrpasses" element={<AdminQrpass />} />
          <Route path="transactions" element={<AdminTransactions />} />
          <Route path="users" element={<AdminUser />} />
          <Route path="payments" element={<AdminPayment />} />
          <Route path="notification" element={<AdminNotification />} />


>>>>>>> Stashed changes
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App