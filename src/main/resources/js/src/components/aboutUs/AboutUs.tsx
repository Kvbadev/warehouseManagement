export default function AboutUs() {
    return (
        <div className="flex flex-col items-center h-1/2">
            <h1 className="font-lato text-3xl p-8">About the project</h1>
            <p className="w-3/4">Warehouse Management System (WMS) is a Fullstack Web Application designed to streamline the management of warehouses, offering a robust set of functionalities for efficient organization, inventory tracking, and resource management. With the help of WMS you can visualize, create and edit data retrieved from your warehouse. This version of the app is for desktop only as i plan on building a mobile app with different functionalities such as scanning and adding products in the future. Because of that, I don't recommend viewing the application on the phone. The application rely on spring-boot and react. For database migrations I've chosen liquibase and I'm using it with postgresql for production and h2 database for testing purposes.</p>
        </div>
    )
}