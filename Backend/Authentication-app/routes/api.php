<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\MenuController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });



Route::get('/MenuItems', [MenuController::class, 'AdminMenuItems'])->middleware(['auth:api','isAdmin']);


Route::post('/register', [AuthController::class, 'register']);
Route::get('/getUser', [AuthController::class, 'get_user']);
Route::post('/login', [AuthController::class, 'login']);
Route::put('/update/{user}', [AuthController::class, 'update'])->middleware('auth:api');


