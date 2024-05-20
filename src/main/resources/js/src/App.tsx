import './App.css';
import { Navbar } from './components/navbar/Navbar';
import { Dashboard } from './components/dashboard/Dashboard';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SignIn from './components/signIn/SignIn';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/ReactToastify.css';
import { AuthProvider } from './context/AuthContext';
import { LoggedUserProvider } from './context/LoggedUserContext';
import Home from './components/dashboard/Home';
import Users from './components/dashboard/users/Users';
import Main from './components/Main';
import Profile from './components/profile/Profile';
import UserEdit from './components/dashboard/users/UserEdit';
import Items from './components/dashboard/items/Items';
import ItemEdit from './components/dashboard/items/ItemEdit';
import Parcels from './components/dashboard/parcels/Parcels';
import ParcelEdit from './components/dashboard/parcels/ParcelEdit';
import Deliveries from './components/dashboard/deliveries/Deliveries';
import DeliveryEdit from './components/dashboard/deliveries/DeliveryEdit';
import AboutUs from './components/aboutUs/AboutUs';

function App() {
  return (
    <>
      <AuthProvider>
        <LoggedUserProvider>
          <BrowserRouter>
            <Navbar />
            <Routes>
              <Route path='/' element={<Main />} />
              <Route path='dashboard' element={<Dashboard />}>
                <Route path='home' element={<Home />} />
                <Route path='users' element={<Users />} />
                <Route path='users/:id' element={<UserEdit />} />
                <Route path='items' element={<Items />} />
                <Route path='items/:id' element={<ItemEdit />} />
                <Route path='parcels' element={<Parcels />} />
                <Route path='parcels/:id' element={<ParcelEdit />} />
                <Route path='deliveries' element={<Deliveries />} />
                <Route path='deliveries/:id' element={<DeliveryEdit/>} />
              </Route>
              <Route path="/sign-in" element={<SignIn />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/about-us" element={<AboutUs />} />
            </Routes>
          </BrowserRouter>
          <ToastContainer />
        </LoggedUserProvider>
      </AuthProvider>
    </>
  );
}

export default App;
