import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Header from "./components/Header";
import Footer from "./components/Footer";


function App() {
  return (
    <div className="min-h-screen flex flex-col ">
    <Router>
      <Header />
      <Footer />
    </Router>
    </div>
  );
}

export default App;
