-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 10, 2018 at 03:56 AM
-- Server version: 10.1.28-MariaDB
-- PHP Version: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cashinmerchant`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_invoice`
--

CREATE TABLE `tb_invoice` (
  `invoice_code` varchar(20) NOT NULL DEFAULT '0',
  `invoice_index` int(11) NOT NULL DEFAULT '0',
  `zptransid` varchar(30) NOT NULL DEFAULT '',
  `amount` varchar(10) NOT NULL DEFAULT '0',
  `reciever` varchar(20) NOT NULL,
  `transfer_type` varchar(10) NOT NULL DEFAULT '0',
  `machine_name` varchar(10) NOT NULL DEFAULT '',
  `date_order` varchar(20) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(2) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tb_invoice`
--

INSERT INTO `tb_invoice` (`invoice_code`, `invoice_index`, `zptransid`, `amount`, `reciever`, `transfer_type`, `machine_name`, `date_order`, `timestamp`, `status`) VALUES
('1801100001', 1, '', '11111', 'tamvh', '1', 'vng', '2018-01-10', '2018-01-09 17:03:44', 0);

-- --------------------------------------------------------

--
-- Table structure for table `tb_machine`
--

CREATE TABLE `tb_machine` (
  `machine_name` varchar(10) NOT NULL,
  `address` varchar(30) NOT NULL,
  `time_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `account_id` int(11) NOT NULL,
  `account_name` varchar(64) NOT NULL,
  `password` varchar(64) DEFAULT '123456',
  `account_type` tinyint(3) NOT NULL DEFAULT '0',
  `reset` tinyint(1) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for table `tb_invoice`
--
ALTER TABLE `tb_invoice`
  ADD PRIMARY KEY (`invoice_code`);

--
-- Indexes for table `account`
--

ALTER TABLE `account`
  ADD PRIMARY KEY (`account_id`);

-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `account_id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
