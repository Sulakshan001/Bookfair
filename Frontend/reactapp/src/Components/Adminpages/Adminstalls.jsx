import React, { useEffect, useState, useMemo } from "react";
import {
  Search,
  Filter,
  Edit3,
  Trash2,
  Store,
  MapPin,
  CheckCircle,
  XCircle,
  AlertTriangle,
  ChevronLeft,
  ChevronRight,
  RefreshCw,
  Plus,
} from "lucide-react";
import Admin from "../../services/Admin";
import { motion, AnimatePresence } from "framer-motion";

const stallStatuses = ["AVAILABLE", "OCCUPIED", "MAINTENANCE", "DISABLED"];

const Adminstalls = () => {
  const [stalls, setStalls] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Filters
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedStatus, setSelectedStatus] = useState("ALL");

  // Modals
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showStatusModal, setShowStatusModal] = useState(false);
  const [selectedStall, setSelectedStall] = useState(null);
  const [newStatus, setNewStatus] = useState("");

  // Notification
  const [notification, setNotification] = useState(null);

  const showNotification = (message, type) => {
    setNotification({ message, type });
    setTimeout(() => setNotification(null), 4000);
  };

  const fetchStalls = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await Admin.getAllStalls();
      setStalls(data || []);
    } catch (err) {
      console.error("Error fetching stalls:", err);
      setError("Failed to load stalls. Please try again.");
      setStalls([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStalls();
  }, []);

  // Filtered stalls
  const filteredStalls = useMemo(() => {
    let data = [...stalls];

    // Filter by status
    if (selectedStatus !== "ALL") {
      data = data.filter((stall) => stall.status === selectedStatus);
    }

    // Search by name or location
    const q = searchTerm.trim().toLowerCase();
    if (q) {
      data = data.filter((stall) =>
        (stall.name || "").toLowerCase().includes(q) ||
        (stall.location || "").toLowerCase().includes(q)
      );
    }

    return data;
  }, [stalls, selectedStatus, searchTerm]);

  const handleDeleteStall = async () => {
    if (!selectedStall) return;

    try {
      await Admin.deleteStall(selectedStall.id);
      showNotification("Stall deleted successfully", "success");
      setShowDeleteModal(false);
      setSelectedStall(null);
      fetchStalls(); // Refresh list
    } catch (err) {
      console.error("Error deleting stall:", err);
      showNotification("Failed to delete stall", "error");
    }
  };

  const handleUpdateStatus = async () => {
    if (!selectedStall || !newStatus) return;

    try {
      await Admin.updateStallStatus(selectedStall.id, newStatus);
      showNotification("Stall status updated successfully", "success");
      setShowStatusModal(false);
      setSelectedStall(null);
      setNewStatus("");
      fetchStalls(); // Refresh list
    } catch (err) {
      console.error("Error updating stall status:", err);
      showNotification("Failed to update stall status", "error");
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "AVAILABLE":
        return "text-green-600 bg-green-100";
      case "OCCUPIED":
        return "text-blue-600 bg-blue-100";
      case "MAINTENANCE":
        return "text-yellow-600 bg-yellow-100";
      case "DISABLED":
        return "text-red-600 bg-red-100";
      default:
        return "text-slate-400 bg-slate-900/20";
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "AVAILABLE":
        return <CheckCircle className="w-4 h-4" />;
      case "OCCUPIED":
        return <Store className="w-4 h-4" />;
      case "MAINTENANCE":
        return <AlertTriangle className="w-4 h-4" />;
      case "DISABLED":
        return <XCircle className="w-4 h-4" />;
      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 p-6">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-white mb-2">Stall Management</h1>
          <p className="text-slate-300">Manage and monitor all stalls in the system</p>
        </div>

        {/* Filters and Search */}
        <div className="bg-slate-900/50 rounded-lg shadow-sm border border-slate-700/50 p-6 mb-6">
          <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
            <div className="flex flex-col md:flex-row gap-4 flex-1">
              {/* Search */}
              <div className="relative flex-1 max-w-md">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-slate-400 w-5 h-5" />
                <input
                  type="text"
                  placeholder="Search stalls by name or location..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 bg-transparent border border-slate-600 rounded-lg text-white placeholder-slate-400 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              {/* Status Filter */}
              <div className="flex items-center gap-2">
                <Filter className="text-slate-400 w-5 h-5" />
                <select
                  value={selectedStatus}
                  onChange={(e) => setSelectedStatus(e.target.value)}
                  className="px-3 py-2 bg-transparent border border-slate-600 rounded-lg text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="ALL">All Statuses</option>
                  {stallStatuses.map((status) => (
                    <option key={status} value={status}>
                      {status}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            {/* Refresh Button */}
            <button
              onClick={fetchStalls}
              disabled={loading}
              className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
              Refresh
            </button>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-6">
          {stallStatuses.map((status) => {
            const count = stalls.filter((stall) => stall.status === status).length;
            return (
              <div key={status} className="bg-slate-900/50 rounded-lg shadow-sm border border-slate-700/50 p-6">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-slate-400">{status}</p>
                    <p className="text-2xl font-bold text-white">{count}</p>
                  </div>
                  <div className={`p-3 rounded-full ${getStatusColor(status)}`}>
                    {getStatusIcon(status)}
                  </div>
                </div>
              </div>
            );
          })}
        </div>

        {/* Stalls Table */}
        <div className="bg-slate-900/50 rounded-lg shadow-sm border border-slate-700/50 overflow-hidden">
          {loading ? (
            <div className="flex items-center justify-center py-12">
              <RefreshCw className="w-8 h-8 animate-spin text-blue-600" />
              <span className="ml-2 text-slate-300">Loading stalls...</span>
            </div>
          ) : error ? (
            <div className="flex items-center justify-center py-12">
              <AlertTriangle className="w-8 h-8 text-red-600" />
              <span className="ml-2 text-red-600">{error}</span>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-slate-800/50 border-b border-slate-700/50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-slate-400 uppercase tracking-wider">
                      Stall Details
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-slate-400 uppercase tracking-wider">
                      Status
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-slate-400 uppercase tracking-wider">
                      Location
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-slate-400 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-700/50">
                  {filteredStalls.length === 0 ? (
                    <tr>
                      <td colSpan="4" className="px-6 py-12 text-center text-slate-400">
                        No stalls found matching your criteria.
                      </td>
                    </tr>
                  ) : (
                    filteredStalls.map((stall) => (
                      <tr key={stall.id} className="hover:bg-slate-800/30">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center">
                            <div className="flex-shrink-0">
                              <Store className="w-8 h-8 text-slate-400" />
                            </div>
                            <div className="ml-4">
                              <div className="text-sm font-medium text-white">
                                {stall.name || `Stall ${stall.id}`}
                              </div>
                              <div className="text-sm text-slate-400">
                                ID: {stall.id}
                              </div>
                            </div>
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(stall.status)}`}>
                            {getStatusIcon(stall.status)}
                            <span className="ml-1">{stall.status}</span>
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-white">
                          <div className="flex items-center">
                            <MapPin className="w-4 h-4 text-slate-400 mr-1" />
                            {stall.location || "N/A"}
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <div className="flex items-center space-x-2">
                            <button
                              onClick={() => {
                                setSelectedStall(stall);
                                setNewStatus(stall.status);
                                setShowStatusModal(true);
                              }}
                              className="text-blue-400 hover:text-blue-300 p-1 rounded"
                              title="Update Status"
                            >
                              <Edit3 className="w-4 h-4" />
                            </button>
                            <button
                              onClick={() => {
                                setSelectedStall(stall);
                                setShowDeleteModal(true);
                              }}
                              className="text-red-400 hover:text-red-300 p-1 rounded"
                              title="Delete Stall"
                            >
                              <Trash2 className="w-4 h-4" />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Delete Modal */}
        <AnimatePresence>
          {showDeleteModal && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
            >
              <motion.div
                initial={{ scale: 0.9, opacity: 0 }}
                animate={{ scale: 1, opacity: 1 }}
                exit={{ scale: 0.9, opacity: 0 }}
                className="bg-slate-900 rounded-lg p-6 max-w-md w-full mx-4 border border-slate-800"
              >
                <h3 className="text-lg font-medium text-white mb-4">Delete Stall</h3>
                <p className="text-slate-300 mb-6">
                  Are you sure you want to delete stall "{selectedStall?.name || `Stall ${selectedStall?.id}`}"?
                  This action cannot be undone.
                </p>
                <div className="flex justify-end space-x-3">
                  <button
                    onClick={() => setShowDeleteModal(false)}
                    className="px-4 py-2 text-slate-300 bg-slate-700 rounded-lg hover:bg-gray-600"
                  >
                    Cancel
                  </button>
                  <button
                    onClick={handleDeleteStall}
                    className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
                  >
                    Delete
                  </button>
                </div>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Status Update Modal */}
        <AnimatePresence>
          {showStatusModal && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
            >
              <motion.div
                initial={{ scale: 0.9, opacity: 0 }}
                animate={{ scale: 1, opacity: 1 }}
                exit={{ scale: 0.9, opacity: 0 }}
                className="bg-slate-900 rounded-lg p-6 max-w-md w-full mx-4 border border-slate-800"
              >
                <h3 className="text-lg font-medium text-white mb-4">Update Stall Status</h3>
                <p className="text-slate-300 mb-4">
                  Update status for stall "{selectedStall?.name || `Stall ${selectedStall?.id}`}"
                </p>
                <select
                  value={newStatus}
                  onChange={(e) => setNewStatus(e.target.value)}
                  className="w-full px-3 py-2 bg-transparent border border-slate-600 rounded-lg text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent mb-6"
                >
                  {stallStatuses.map((status) => (
                    <option key={status} value={status}>
                      {status}
                    </option>
                  ))}
                </select>
                <div className="flex justify-end space-x-3">
                  <button
                    onClick={() => setShowStatusModal(false)}
                    className="px-4 py-2 text-slate-300 bg-slate-700 rounded-lg hover:bg-gray-600"
                  >
                    Cancel
                  </button>
                  <button
                    onClick={handleUpdateStatus}
                    className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                  >
                    Update
                  </button>
                </div>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>

        {/* Notification */}
        <AnimatePresence>
          {notification && (
            <motion.div
              initial={{ opacity: 0, y: -50 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -50 }}
              className={`fixed top-4 right-4 z-50 px-6 py-3 rounded-lg shadow-lg ${
                notification.type === "success"
                  ? "bg-green-500 text-white"
                  : "bg-red-500 text-white"
              }`}
            >
              {notification.message}
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </div>
  );
};

export default Adminstalls;