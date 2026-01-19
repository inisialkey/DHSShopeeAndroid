package com.fungsitama.dhsshopee.util;

public class ApiConfig {
    public static final String URL_API = "https://dhs.api.mycargotracking.com/api/";

    //Registrasi
    public static final String Registrasi = URL_API +"modules/global/register";
    //ActivationRegsitrasi
    public static final String ActivationRegsitrasi = URL_API +"modules/global/register/activation";
    //ResendActivation
    public static final String ResendActivation = URL_API +"modules/global/register/resend";
    //InfoAkun
    public static final String InfoAkun =  URL_API +"modules/global/register/info";
    //Login
    public static final String Login =  URL_API +"core/private/login/android";
    //Detail User
    public static final String Get_Detail_User = URL_API + "modules/private/m_user/my";

    /*DRIVER*/
    //Daftar Kendaraan
    public static final String DaftarKendaraan = URL_API + "modules/private/m_vehicle/list";
    //Tambah Kendaraan
    public static final String TambahKendaraan = URL_API + "modules/private/m_vehicle/create";
    //Daftar Pengiriman
    public static final String TambahPengiriman = URL_API + "modules/private/tracking/create";
    //Daftar Seluruh List Tracking
    public static final String DaftarTracking = URL_API + "modules/private/tracking/list";
    //Daftar Pengiriman
    public static final String DaftarPengiriman = URL_API + "modules/private/tracking/listPengiriman";
    //Daftar Retur
    public static final String DaftarRetur = URL_API + "/modules/private/tracking/listRetur";
    //Daftar List Master DC / Tujuan
    public static final String DaftarTujuan = URL_API + "modules/private/m_dc/list";
    //Daftar List Nomor DO
    public static final String DaftarNomorDO = URL_API + "modules/private/tracking/do/list";
    //Tambah Nomor DO
    public static final String TambahNomorDO = URL_API + "modules/private/tracking/do/create";
    //Scan Nomor DO Bay QR Code
    public static final String ScaneNomorDOByQRCode = URL_API + "modules/private/tracking/do/detail/create";
    //Daftar Nomor DO by QR Code
    public static final String DaftarNomorDOByQRCode = URL_API + "modules/private/tracking/do/detail/list";
    //Detail Nomor Detail
    public static final String DetailDO = URL_API + "modules/private/tracking/do/get";
    //Daftar Scan Penerimaan Nomor DO
    //Update Status ke OTW
    public static final String UpdateStatusOTW = URL_API + "modules/private/tracking/outOrigin";
    //Update Status ke Delevered
    public static final String UpdateStatusDelevered = URL_API + "modules/private/tracking/do/detail/scan/inDestination";
    //Update Status ke Retur
    public static final String UpdateStatusRetur = URL_API + "modules/private/tracking/retur";
    //Hapus BarCode
    public static final String HapusBarCode = URL_API + "modules/private/tracking/do/detail/destroy";


    /*DASHBOARD*/
    //Dashboard DHS loading and unloading
    public static final String dashboardLoadingAndUnloading = URL_API + "modules/private/dashboard/android";

    /*DHS WAREHOUSE*/
    //Daftar Warehouse/Gudang
    public static final String listWarehouseGudang = URL_API + "modules/private/m_warehouse/list";

    /*LOADING*/
    //Daftar Loading
    public static final String DaftarLoading = URL_API + "modules/private/tr_cargo/list";
    //Detail Daftar Loading
    public static final String DetailDaftarLoading = URL_API + "modules/private/tr_cargo/get";
    //Hapus Daftar Loading
    public static final String HapusDaftarLoading = URL_API + "modules/private/tr_cargo/destroy";
    //Tambah Barang Loading
    public static final String TambahLoading = URL_API + "modules/private/tr_cargo/create";
    //Scan Barang Loading
    public static final String ScanLoading = URL_API + "modules/private/tr_cargo/detail/create";
    //Daftar Barcode Loading
    public static final String DaftarBarcodeLoading = URL_API + "modules/private/tr_cargo/detail/list";
    //Jumlah Barcode Loading
    public static final String JumlahBarcodeLoading = URL_API + "modules/private/tr_cargo/detail/count";
    //Delete Barcode Loading
    public static final String DeleteBarcodeLoading = URL_API + "modules/private/tr_cargo/detail/destroy";
    //History Delete Barcode Loading
    public static final String HistoryDeleteBarcodeLoading = URL_API + "modules/private/tr_cargo/detail/listDestroy";
    //Update barang loading ke Unloading
    public static final String UpdateUnloading = URL_API + "modules/private/tr_cargo/detail/unloading";
    //Upload Foto
    public static final String UploadFile = URL_API + "modules/private/tr_cargo/detail/uploadFile/";
    //Detail Barcode Manual
    public static final String detailBarcodeManual = URL_API + "modules/private/tr_cargo/detail/get";

}