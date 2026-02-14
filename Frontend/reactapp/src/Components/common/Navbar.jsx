import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import UserAccountApi from "../../services/UserAccountApi";

const Navbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();

  const isAuthenticated = UserAccountApi.isAuthenticated();
  const isAdmin = UserAccountApi.isAdmin();
  const isVendor = UserAccountApi.isVendor();
  const isPublisher = UserAccountApi.isPublisher?.() ?? false; // safe if not implemented yet

  const handleLogout = () => {
    if (window.confirm("Are you sure you want to logout?")) {
      UserAccountApi.logout();
      navigate("/sign-in"); // ✅ make consistent with your links
    }
  };

  const menuVariants = {
    hidden: { x: "100%" },
    visible: {
      x: 0,
      transition: { type: "spring", stiffness: 300, damping: 30 },
    },
    exit: {
      x: "100%",
      transition: { type: "spring", stiffness: 300, damping: 30 },
    },
  };

  return (
    <>
      <nav className="bg-slate-800 shadow-lg">
        <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">
          <Link to="/" className="text-2xl font-bold text-white tracking-wide">
            BookFair<span className="text-sky-400">.</span>
          </Link>

          {/* Desktop Menu */}
          <div className="hidden md:flex items-center gap-8 text-sm font-medium text-white">
            {["home", "about", "service", "contact"].map((item) => (
              <Link
                key={item}
                to={`/${item}`}
                className="hover:text-sky-400 transition-colors duration-200"
              >
                {item.charAt(0).toUpperCase() + item.slice(1)}
              </Link>
            ))}

            {isAuthenticated ? (
              <>
                {isVendor && (
                  <Link to="/Booking" className="hover:text-sky-400 transition-colors">
                    Booking
                  </Link>
                )}

                {isPublisher && (
                  <Link to="/Booking" className="hover:text-sky-400 transition-colors">
                    Booking
                  </Link>
                )}

                {isAdmin && (
                  <Link to="/admin" className="hover:text-sky-400 transition-colors">
                    Admin
                  </Link>
                )}



                <button
                  onClick={handleLogout}
                  className="ml-4 px-4 py-2 border border-red-400 text-red-400 rounded hover:bg-red-500 hover:text-white transition-colors"
                >
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link
                  to="/sign-in"
                  className="px-4 py-2 border border-sky-300 text-sky-300 rounded hover:bg-sky-300 hover:text-slate-900 transition-colors"
                >
                  Sign In
                </Link>

                <Link
                  to="/sign-up"
                  className="px-4 py-2 bg-sky-500 text-white rounded hover:bg-sky-600 transition-colors"
                >
                  Sign Up
                </Link>
              </>
            )}
          </div>

          {/* Mobile Menu Button */}
          <button
            className="md:hidden text-white text-2xl"
            onClick={() => setMenuOpen(true)}
          >
            ☰
          </button>
        </div>
      </nav>

      {/* Mobile Menu */}
      <AnimatePresence>
        {menuOpen && (
          <motion.div
            className="fixed top-0 right-0 h-full w-72 bg-slate-800 text-white z-50 shadow-lg"
            variants={menuVariants}
            initial="hidden"
            animate="visible"
            exit="exit"
          >
            <div className="flex justify-between items-center p-6 border-b border-white/20">
              <span className="font-semibold text-lg">Menu</span>
              <button className="text-xl" onClick={() => setMenuOpen(false)}>
                ✕
              </button>
            </div>

            <div className="flex flex-col gap-6 p-6 text-sm">
              {["home", "about", "service", "contact"].map((item) => (
                <Link
                  key={item}
                  to={`/${item}`}
                  onClick={() => setMenuOpen(false)}
                  className="hover:text-sky-400 transition-colors"
                >
                  {item.charAt(0).toUpperCase() + item.slice(1)}
                </Link>
              ))}

              {isAuthenticated ? (
                <>
                  {isVendor && (
                    <Link to="/booking" onClick={() => setMenuOpen(false)}>
                      Booking
                    </Link>
                  )}
                  {isEmployee && (
                    <Link to="/booking" onClick={() => setMenuOpen(false)}>
                      Booking
                    </Link>
                  )}
                  {isAdmin && (
                    <Link to="/admin" onClick={() => setMenuOpen(false)}>
                      Admin
                    </Link>
                  )}



                  <button
                    onClick={() => {
                      handleLogout();
                      setMenuOpen(false);
                    }}
                    className="mt-4 border border-red-400 text-red-400 py-2 rounded hover:bg-red-500 hover:text-white transition-colors"
                  >
                    Logout
                  </button>
                </>
              ) : (
                <>
                  <Link to="/sign-in" onClick={() => setMenuOpen(false)}>
                    Sign In
                  </Link>
                  <Link to="/sign-up" onClick={() => setMenuOpen(false)}>
                    Sign Up
                  </Link>
                </>
              )}
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </>
  );
};

export default Navbar;
