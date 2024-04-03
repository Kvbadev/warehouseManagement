import React, { useContext } from 'react';
import './App.css';
import { Navbar } from './components/navbar/Navbar';
import { Dashboard } from './components/dashboard/Dashboard';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import SignIn from './components/signIn/SignIn';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/ReactToastify.css';
import { useAuth, AuthProvider } from './context/AuthContext';
import Home from './components/dashboard/Home';

function App() {
  const authContext = useAuth();
  return (
    <>
      <AuthProvider>
        <BrowserRouter>
          <Navbar />
          <Routes>
            <Route path='dashboard' element={<Dashboard />}>
              <Route path='home' element={<Home />} />
              <Route path='users'/>
              <Route path='users/:id'/>
              <Route path='items'/>
              <Route path='items/:id'/>
              <Route path='parcels' />
              <Route path='parcels/:id'/>
              <Route path='deliveries'/>
              <Route path='deliveries/:id'/>
            </Route>
            <Route path="/sign-in" element={<SignIn />} />
            {/* <Route
              path="*"
              element={<Navigate to="/" replace />}
            /> */}
          </Routes>
        </BrowserRouter>
      </AuthProvider>
      <ToastContainer />
    </>
  );
}

export default App;
